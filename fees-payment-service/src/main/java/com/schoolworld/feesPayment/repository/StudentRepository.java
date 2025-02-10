package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {

    @Modifying
    @Query("UPDATE Student s SET s.isFeesFullyPaid = :paidStatus, s.outstandingFees = :remainingFees WHERE s.id = :standardId")
    void updateStatusAndRemainingFees(@Param("paidStatus") final boolean paidStatus,
                                      @Param("remainingFees") final int remainingFees,
                                      @Param("standardId") final String standardId);

}
