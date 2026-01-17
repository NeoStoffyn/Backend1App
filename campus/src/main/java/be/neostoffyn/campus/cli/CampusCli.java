package be.neostoffyn.campus.cli;

import be.neostoffyn.campus.cli.screens.CampusScreen;
import be.neostoffyn.campus.cli.screens.UsersScreen;
import be.neostoffyn.campus.cli.screens.ReservationScreen;
import be.neostoffyn.campus.service.CampusService;
import be.neostoffyn.campus.service.UserService;
import be.neostoffyn.campus.service.ReservationService;
import be.neostoffyn.campus.service.RoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CampusCli implements CommandLineRunner {

    private final CampusScreen campusScreen;
    private final UsersScreen usersScreen;
    private final ReservationScreen reservationScreen;

    private final Scanner scanner = new Scanner(System.in);

    public CampusCli(
            CampusService campusService,
            UserService userService,
            ReservationService reservationService,
            RoomService roomService
    ) {
        this.campusScreen = new CampusScreen(campusService, roomService, scanner);
        this.usersScreen = new UsersScreen(userService, reservationService, scanner);
        this.reservationScreen = new ReservationScreen(userService, roomService, reservationService, scanner);
    }

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. View campuses");
            System.out.println("2. View users");
            System.out.println("3. Reservations");
            System.out.println("4. Exit");
            System.out.print("Select option: ");

            int choice = Cli.readInt(scanner);

            try {
                switch (choice) {
                    case 1 -> campusScreen.show();
                    case 2 -> usersScreen.show();
                    case 3 -> reservationScreen.show();
                    case 4 -> {
                        System.out.println("CLI closed.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (RuntimeException ex) {
                Cli.error(ex);
            }
        }
    }
}

