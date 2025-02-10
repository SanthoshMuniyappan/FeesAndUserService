package com.schoolworld.user.repository;

import com.schoolworld.user.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    @Query("select u from UserInfo u where u.isActive=false")
    List<UserInfo> retrieveAllUserInfoValues();

    @Query(nativeQuery = true,value = "select * from user_info where user_id=:userId AND is_delete=false")
    Optional<UserInfo> getUserExist(@Param("userId")final String userId);

    Page<UserInfo> findByIsDeleteFalse(Pageable pageable);
}
