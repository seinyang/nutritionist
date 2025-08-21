package sein.nutritionist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sein.nutritionist.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

Optional<User> findByUserId(String userId);

}
