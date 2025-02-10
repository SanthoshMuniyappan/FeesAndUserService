package com.schoolworld.user.repository;

import com.schoolworld.user.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,String> {

    @Query(nativeQuery = true,value = "select * from teacher where reg_number=:regNumber AND is_delete=false")
    Teacher getTeacherByRegNumber(@Param("regNumber") String regNumber);
}
