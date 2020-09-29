package co.edu.funlam.sistemainvestigativo.model.entities.access;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T01_ACCESS_TOKEN")
@Getter
@Setter
@NoArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "T01_ID")
    private Long id;

    @Column(name = "T01_TOKEN")
    private String token;

    @Column(name = "T01_CREATION_DATE")
    private Date creationDate;

    @OneToOne
    private User user;
}
