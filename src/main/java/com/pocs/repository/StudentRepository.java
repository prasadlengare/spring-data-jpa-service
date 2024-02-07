package com.pocs.repository;

import com.pocs.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByEmail(String mail);

    //JPQL
    @Query("SELECT s FROM Student s WHERE s.id = ?1")
    Optional<Student> findStudentByJPQL(Long id);

    //Native Query
    @Query(value = "SELECT * FROM Student WHERE id = ?1", nativeQuery = true)
    Optional<Student> findStudentByNativeQuery(Long id);

    @Query(value = "SELECT * FROM Student WHERE first_name = :firstname", nativeQuery = true)
    Optional<Student> findStudentByNamedParameter(@Param("firstname") String firstName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = ?1")
    int deleteStudentById(Long id);
}
