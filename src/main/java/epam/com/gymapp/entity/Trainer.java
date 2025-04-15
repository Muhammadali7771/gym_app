package epam.com.gymapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingType specialization;
    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees;
}
