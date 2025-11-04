package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.Campus.CampusInvalidDataException;
import be.neostoffyn.campus.exception.Campus.CampusNotFoundException;
import be.neostoffyn.campus.exception.Room.RoomAlreadyExistsException;
import be.neostoffyn.campus.exception.Room.RoomInvalidDataException;
import be.neostoffyn.campus.exception.Room.RoomNotFoundException;
import be.neostoffyn.campus.model.Campus;
import be.neostoffyn.campus.model.Room;
import be.neostoffyn.campus.repository.CampusRepository;
import be.neostoffyn.campus.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final CampusRepository campusRepository;

    public RoomService(RoomRepository roomRepository, CampusRepository campusRepository) {
        this.roomRepository = roomRepository;
        this.campusRepository = campusRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room addRoom(String campusName, Room room) {
        Campus campus = campusRepository.findById(campusName)
                .orElseThrow(() -> new CampusNotFoundException("Campus not found: " + campusName));

        final String roomName = room.getName();

        if (roomName == null || roomName.isBlank())
            throw new RoomInvalidDataException("Room name is required");

        if (room.getType() == null || room.getType().isBlank())
            throw new RoomInvalidDataException("Room type is required");

        if (room.getCapacity() <= 0)
            throw new RoomInvalidDataException("Room capacity is required");

        if (room.getFirstName() == null || room.getFirstName().isBlank())
            throw new RoomInvalidDataException("Responsible first name is required");

        if (room.getLastName() == null || room.getLastName().isBlank())
            throw new RoomInvalidDataException("Responsible last name is required");

        if (room.getFloor() <= 0)
            throw new RoomInvalidDataException("Floor is required");


        if (roomRepository.existsByNameAndCampusName(roomName, campusName)) {
           throw new RoomAlreadyExistsException("Room with name '" + roomName + "' already exists on campus: " + campusName);
        }

        room.setCampus(campus);
        return roomRepository.save(room);
    }


    public List<Room> getRoomsForCampus(
            String campusName,
            LocalDateTime availableFrom,
            LocalDateTime availableUntil,
            Integer minCapacity
    ) {
        if (!campusRepository.existsCampusByName(campusName))
            throw new CampusNotFoundException("Campus not found: " + campusName);



        List<Room> rooms = roomRepository.findByCampus_Name(campusName);

        return rooms.stream()
                .filter(r -> minCapacity == null || r.getCapacity() >= minCapacity)
                .toList();
    }

    public Room getRoomByIdAndCampus(String campusName, Long roomId) {
        if (!campusRepository.existsCampusByName(campusName))
            throw new CampusNotFoundException("Campus not found: " + campusName);

        return roomRepository.findById(roomId)
                .filter(room -> room.getCampus().getName().equals(campusName))
                .orElseThrow(() -> new RoomNotFoundException(
                        "Room " + roomId + " not found for campus " + campusName
                ));
    }
}
