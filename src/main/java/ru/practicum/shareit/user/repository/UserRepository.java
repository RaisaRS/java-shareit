package ru.practicum.shareit.user.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Collection<User> getAllUsers();
//
//    User saveUser(User user);
//
//    User updateUser(User user);
//
//    User deleteUser(long userId);
//
//    User getUserById(long userId);

}
