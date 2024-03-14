package com.example.studentthymeleaf.Repository;

import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsernameAndPassword(String username,String password);
    Optional<User> findByEmail(String email);
    Optional<List<User>> findByUsername(String username);
    Optional<List<User>> findByIdOrUsername(Long userId,String username);
    Integer countByEmail(String email);
    Optional<List<User>> findByIsDeleteFalse();

    @Query("SELECT u FROM User  u WHERE (u.id = :id OR u.username = :name) AND u.isDelete = false")
    Optional<List<User>> findActiveUserQuery(@Param("id") Long id, @Param("name") String name);


}
