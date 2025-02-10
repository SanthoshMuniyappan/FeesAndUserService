package com.schoolworld.user.repository;

import com.schoolworld.user.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query(nativeQuery = true,value = "select * from student where reg_number=:regNumber AND is_delete=false")
    Student getStudentByRegisterNumber(@Param("regNumber") String regNumber);
}
