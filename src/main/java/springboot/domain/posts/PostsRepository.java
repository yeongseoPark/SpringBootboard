package springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts,Long> { // JpaRepository<Entity, Pk클래스> 를 상속받으면 CRUD메소드 자동 생성

}
