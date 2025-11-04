package be.neostoffyn.campus.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Campus {

    @Id
    private String name;

    private String address;
    private int parkingSpots;

    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL)
    private List<Room> rooms;

    public Campus() {}

    public Campus(String name, String address, int parkingSpots) {
        this.name = name;
        this.address = address;
        this.parkingSpots = parkingSpots;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(int parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Transient
    public int getRoomCount() {
        return (rooms != null) ? rooms.size() : 0;
    }
}
