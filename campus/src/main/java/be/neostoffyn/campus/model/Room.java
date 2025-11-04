package be.neostoffyn.campus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private int capacity;
    private String firstName;
    private String lastName;
    private int floor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "campus_name")
    private Campus campus;

    @JsonIgnore
    @ManyToMany(mappedBy = "rooms")
    private List<Reservation> reservations;

    public Room() {}

    public Room(String name, String type, int capacity,
                String firstName, String lastName, int floor, Campus campus) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.firstName = firstName;
        this.lastName = lastName;
        this.floor = floor;
        this.campus = campus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
