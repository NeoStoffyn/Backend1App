package be.neostoffyn.campus.cli.screens;

import be.neostoffyn.campus.cli.Cli;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.model.Room;
import be.neostoffyn.campus.service.CampusService;
import be.neostoffyn.campus.service.RoomService;

import java.util.List;
import java.util.Scanner;

public class CampusScreen {
    private final CampusService campusService;
    private final RoomService roomService;
    private final Scanner scanner;

    public CampusScreen(CampusService campusService, RoomService roomService, Scanner scanner) {
        this.campusService = campusService;
        this.roomService = roomService;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            List<Campus> campuses = campusService.getAllCampuses();

            System.out.println("\n=== CAMPUS SCREEN ===");
            for (int i = 0; i < campuses.size(); i++) {
                System.out.println((i + 1) + ". " + campuses.get(i).getName());
            }
            System.out.println("0. Back");
            System.out.println((campuses.size() + 1) + ". Add campus");
            System.out.println((campuses.size() + 2) + ". Add room");
            System.out.print("Select a campus (number) or option: ");

            int choice = Cli.readInt(scanner);

            if (choice == 0) return;
            else if (choice == campuses.size() + 1) addCampus();
            else if (choice == campuses.size() + 2) addRoom();
            else if (choice >= 1 && choice <= campuses.size()) showCampusDetails(campuses.get(choice - 1));
            else System.out.println("Invalid choice.");
        }
    }

    private void addCampus() {
        System.out.print("Enter campus name: ");
        String name = scanner.nextLine();
        System.out.print("Enter campus address: ");
        String address = scanner.nextLine();
        System.out.print("Enter number of parking spots: ");
        int parkingSpots = Cli.readInt(scanner);

        try {
            Campus saved = campusService.saveCampus(new Campus(name, address, parkingSpots));
            Cli.success("Campus added: " + saved.getName());
        } catch (RuntimeException ex) {
            Cli.error(ex);
        }
    }

    private void addRoom() {
        System.out.print("Enter campus name: ");
        String campusName = scanner.nextLine();

        Room room = new Room();
        System.out.print("Enter room name: ");
        room.setName(scanner.nextLine());
        System.out.print("Enter room type: ");
        room.setType(scanner.nextLine());
        System.out.print("Enter room capacity: ");
        room.setCapacity(Cli.readInt(scanner));
        System.out.print("Enter responsible first name: ");
        room.setFirstName(scanner.nextLine());
        System.out.print("Enter responsible last name: ");
        room.setLastName(scanner.nextLine());
        System.out.print("Enter floor: ");
        room.setFloor(Cli.readInt(scanner));

        try {
            Room saved = roomService.addRoom(campusName, room);
            Cli.success("Room added to campus " + campusName + " with id " + saved.getId());
        } catch (RuntimeException ex) {
            Cli.error(ex);
        }
    }

    private void showCampusDetails(Campus campus) {
        System.out.println("\n=== CAMPUS DETAILS ===");
        System.out.println("Name: " + campus.getName());
        System.out.println("Address: " + campus.getAddress());
        System.out.println("Parking spots: " + campus.getParkingSpots());
        System.out.println("Rooms:");
        try {
            List<Room> rooms = roomService.getRoomsForCampus(campus.getName(), null, null, null);
            if (rooms != null && !rooms.isEmpty()) {
                rooms.forEach(r ->
                        System.out.println(" - " + r.getName() + " (" + r.getType() + ", cap " + r.getCapacity() + ")"));
            } else {
                System.out.println(" - none");
            }
        } catch (RuntimeException ex) {
            Cli.error(ex);
        }
        System.out.println("\n0. Back");
        System.out.print("Choose option: ");
        Cli.readInt(scanner);
    }
}
