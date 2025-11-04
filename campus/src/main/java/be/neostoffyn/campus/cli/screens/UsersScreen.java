package be.neostoffyn.campus.cli.screens;

import be.neostoffyn.campus.cli.Cli; 
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.service.ReservationService;
import be.neostoffyn.campus.service.UserService;

import java.util.List;
import java.util.Scanner;

public class UsersScreen {
    private final UserService userService;
    private final ReservationService reservationService;
    private final Scanner scanner;

    public UsersScreen(UserService userService, ReservationService reservationService, Scanner scanner) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            List<User> users = userService.getAllUsers(null);

            System.out.println("\n=== USERS SCREEN ===");
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                System.out.println((i + 1) + ". [ID: " + u.getId() + "] " + u.getFirstName() + " " + u.getLastName() + " <" + u.getEmail() + ">");
            }
            System.out.println("0. Back");
            System.out.println((users.size() + 1) + ". Add user");
            System.out.print("Select user (number) or option: ");

            int choice = Cli.readInt(scanner);

            if (choice == 0) return;
            else if (choice == users.size() + 1) addUser();
            else if (choice >= 1 && choice <= users.size()) showUserDetails(users.get(choice - 1));
            else System.out.println("Invalid choice.");
        }
    }

    private void addUser() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        try {
            User created = userService.createUser(user);
            Cli.success("User added: " + created.getFirstName() + " " + created.getLastName());
        } catch (RuntimeException ex) {
            Cli.error(ex);
        }
    }

    private void showUserDetails(User user) {
        System.out.println("\n=== USER DETAILS ===");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Reservations:");
        reservationService.getReservationsByUser(user.getId())
                .forEach(r -> System.out.println(" - Reservation #" + r.getId() + " " + r.getStartTime() + " -> " + r.getEndTime()));
        System.out.println("\n0. Back");
        System.out.println("1. Delete user");
        System.out.print("Choose option: ");
        int choice = Cli.readInt(scanner);

        if (choice == 1) {
            try {
                userService.deleteUser(user.getId());
                Cli.success("User deleted.");
            } catch (RuntimeException ex) {
                Cli.error(ex);
            }
        }
    }
}