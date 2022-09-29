package springboot.domain.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Query("SELECT p FROM Alert p where p.user.id = :id")
    List<Alert> findByUser(@Param("id") Long id);
}
