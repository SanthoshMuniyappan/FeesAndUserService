package com.schoolworld.user.repository;

import com.schoolworld.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query(nativeQuery = true ,value = "select * from role where user_id=:userId AND is_delete=false")
    Role findRoleByUserId(@Param("userId") final String userId);

    @Query(nativeQuery = true, value = "select role from role where user_id=:id AND is_delete=false")
    List<String> getUserRole(@Param("id") final String id);

    @Query("select r from Role r where r.isActive=false")
    List<Role> retrieveAllRoles();
}
