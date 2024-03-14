package com.example.studentthymeleaf.Repository;

import com.example.studentthymeleaf.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course,Long> {

    Optional<Course> findByName(String name);
    Optional<List<Course>> findByIdOrName(Long courseId,String name);
    Optional<List<Course>> findByStatusAndEnabledIsTrue(String status);
    Optional<List<Course>> findCourseByEnabledIsTrue();

    @Query("SELECT c FROM Course c WHERE (c.id = :id OR c.name = :name) AND c.enabled = true")
    Optional<List<Course>> findCoursesByIdOrNameAndEnabledTrue(@Param("id") Long id, @Param("name") String name);
}
