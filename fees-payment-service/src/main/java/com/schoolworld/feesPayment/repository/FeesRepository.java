package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeesRepository extends JpaRepository<Fees, String> {

    @Query(nativeQuery = true, value = "select f.fees_amount from fees f where f.standard_id=:id AND f.fees_category=:feesCategory AND is_delete=false")
    Integer StudentFees(@Param("id") String id, @Param("feesCategory") String feesCategory);

    @Query(nativeQuery = true, value = "select * from fees f where f.standard_id=:standardId AND is_delete=false")
    List<Fees> findAllByStandard(final String standardId);

    @Query("select f from Fees f where f.isDelete=false")
    List<Fees> retrieveAll();

    @Query(nativeQuery = true, value = "select fees_amount from fees where standard_id=:standardId AND fees_category=:feesType")
    Integer getStudentFeesTypeAmount(@Param("standardId") final String standardId, @Param("feesType") final String feesType);
}
