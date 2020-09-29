package co.edu.funlam.sistemainvestigativo.model.entities.access;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "T03_ROLE")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "T03_ID")
    private Long id;

    @Column(name = "T03_NAME")
    private String name;

    @Column(name = "T03_COMMENT")
    private String comment;
}
