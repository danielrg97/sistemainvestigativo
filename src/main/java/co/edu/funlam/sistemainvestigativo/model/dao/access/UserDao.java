package co.edu.funlam.sistemainvestigativo.model.dao.access;

import co.edu.funlam.sistemainvestigativo.model.entities.access.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
