package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query(nativeQuery = true, value = "select * from payment p where p.student_id=:studentId AND is_delete=false")
    List<Payment> retrieveByStudentId(final String studentId);

    @Query("select p from Payment p where p.isDelete=false")
    List<Payment> retrieveAll();

    @Query(nativeQuery = true, value = "select pay_amount from payment where student_id=:studentId AND payment_type=:feesType")
    List<Integer> getStudentPaymentByFeesType(@Param("studentId") final String studentId, @Param("feesType") final String feesType);

    Page<Payment> findByIsDeleteFalse(Pageable pageable);
}
