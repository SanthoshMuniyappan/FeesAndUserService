package com.schoolworld.feesPayment.repository;

import com.schoolworld.feesPayment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query(nativeQuery = true, value = "select role from role where user_id=:id AND is_delete=false")
    List<String> getUserRole(@Param("id") final String id);
}
