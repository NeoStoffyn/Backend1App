package be.neostoffyn.campus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;

    @ManyToMany
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reservation() {}

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
