package be.neostoffyn.campus.dto;

import be.neostoffyn.campus.model.Campus;

public record CampusDto(String name, String address, int parkingSpots, int roomCount) {
    public static CampusDto from(Campus campus) {
        return new CampusDto(
                campus.getName(),
                campus.getAddress(),
                campus.getParkingSpots(),
                campus.getRoomCount()
        );
    }
}
