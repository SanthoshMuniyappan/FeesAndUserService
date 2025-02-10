package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM discount WHERE student_id = :studentId")
    Discount findDiscountByStudentId(@Param("studentId") final String studentId);

    @Query("select d from Discount d where d.isDelete=false")
    List<Discount> retrieveAll();

    Page<Discount> findByIsDeleteFalse(Pageable pageable);
}
