package co.edu.funlam.sistemainvestigativo.model.entities.access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "T02_USER")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "T02_ID")
    private Long id;

    @Column(name = "T02_NAMES")
    private String names;

    @Column(name = "T02_LAST_NAMES")
    private String lastNames;

    @Column(name = "T02_EMAIL")
    private String email;

    @Column(name = "T02_USER_NAME")
    private String userName;

    @Column(name = "T02_PASSWORD")
    private String password;

    @OneToOne
    private Role role;
}
