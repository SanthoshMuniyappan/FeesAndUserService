package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardRepository extends JpaRepository<Standard, String> {
}
