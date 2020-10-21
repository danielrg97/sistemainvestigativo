package co.edu.funlam.sistemainvestigativo.model.dao.access;

import co.edu.funlam.sistemainvestigativo.model.entities.access.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByUserNameAndPassword(String userName, String password);
}
