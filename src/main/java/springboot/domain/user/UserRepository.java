package springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // 이미 있는 사용자인지, 처음 가입하는 사용자인지 판단하기 위한 메소드
}
