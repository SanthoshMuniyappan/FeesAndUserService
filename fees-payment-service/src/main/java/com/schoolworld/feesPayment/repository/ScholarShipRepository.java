package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.ScholarShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarShipRepository extends JpaRepository<ScholarShip, String> {

    @Query(nativeQuery = true, value = "select s.scholar_ship_amount from scholar_ship s where s.student_id=:studentId")
    Integer findScholarShipAmountByStudentId(@Param("studentId") final String studentId);

    @Query("select s from ScholarShip s where s.isDelete=false")
    List<ScholarShip> retrieveAll();

    Page<ScholarShip> findByIsDeleteFalse(Pageable pageable);
}
