package be.neostoffyn.campus.cli.screens;

import be.neostoffyn.campus.cli.Cli;
import be.neostoffyn.campus.model.Reservation;
import be.neostoffyn.campus.model.Room;
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.service.ReservationService;
import be.neostoffyn.campus.service.RoomService;
import be.neostoffyn.campus.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservationScreen {

    private final UserService userService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final Scanner scanner;

    private static final DateTimeFormatter INPUT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReservationScreen(UserService userService, RoomService roomService, ReservationService reservationService, Scanner scanner) {
        this.userService = userService;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    public void show() {
        try {
            List<User> users = userService.getAllUsers(null);
            if (users == null || users.isEmpty()) {
                System.out.println("No users found. Please add a user first.");
                return;
            }

            System.out.println("\n=== USERS ===");
            users.forEach(u -> System.out.println(" - ID: " + u.getId() + " | " + u.getFirstName() + " " + u.getLastName() + " <" + u.getEmail() + ">"));
            System.out.print("Enter user ID: ");
            int userId = Cli.readInt(scanner);

            User user = users.stream()
                    .filter(u -> u.getId() != null && u.getId().intValue() == userId)
                    .findFirst()
                    .orElse(null);
            if (user == null) {
                System.out.println("User ID not found.");
                return;
            }

            List<Room> rooms = roomService.getAllRooms();
            if (rooms == null || rooms.isEmpty()) {
                System.out.println("No rooms available.");
                return;
            }

            System.out.println("\nAvailable rooms:");
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                System.out.println((i + 1) + ". [ID: " + room.getId() + "] " + room.getName() + " (" + room.getType() + ", capacity: " + room.getCapacity() + ")");
            }

            System.out.print("Enter start time (YYYY-MM-DD HH:MM): ");
            String startTimeStr = scanner.nextLine();
            System.out.print("Enter end time (YYYY-MM-DD HH:MM): ");
            String endTimeStr = scanner.nextLine();

            LocalDateTime start;
            LocalDateTime end;
            try {
                start = LocalDateTime.parse(startTimeStr, INPUT_FMT);
                end = LocalDateTime.parse(endTimeStr, INPUT_FMT);
            } catch (DateTimeParseException e) {
                Cli.error("Invalid date format. Use YYYY-MM-DD HH:MM");
                return;
            }


            List<Room> selectedRooms = new ArrayList<>();
            while (true) {
                System.out.print("Select a room (number) to add, or 0 to finish: ");
                int roomChoice = Cli.readInt(scanner);

                if (roomChoice == 0) break;
                if (roomChoice < 1 || roomChoice > rooms.size()) {
                    System.out.println("Invalid room selection.");
                    continue;
                }

                Room selectedRoom = rooms.get(roomChoice - 1);
                if (selectedRooms.contains(selectedRoom)) {
                    System.out.println("Room already selected.");
                    continue;
                }
                selectedRooms.add(selectedRoom);
                Cli.success("Added room: " + selectedRoom.getName());
            }

            if (selectedRooms.isEmpty()) {
                Cli.error("At least one room is required to create a reservation.");
                return;
            }

            Reservation reservation = new Reservation();
            reservation.setStartTime(start);
            reservation.setEndTime(end);
            reservation.setRooms(selectedRooms);

            try {
                Reservation savedReservation = reservationService.createReservation(user.getId(), reservation);
                Cli.success("Reservation created: #" + savedReservation.getId() + " with " + selectedRooms.size() + " room(s).");
            } catch (RuntimeException ex) {
                Cli.error(ex);
            }

        } catch (RuntimeException ex) {
            Cli.error(ex);
        }
    }
}