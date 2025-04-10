package epam.com.gym_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Setter
@Immutable
@Table(name = "training_types")
public class TrainingType {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   @Column(name = "training_type_name", nullable = false)
   private String trainingTypeName;
}
