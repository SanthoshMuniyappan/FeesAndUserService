package com.schoolworld.user.repository;

import com.schoolworld.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    List<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    @Query("select u from User u where u.isActive=false")
    List<User> retrieveAllUsers();

    Page<User> findByIsDeleteFalse(Pageable pageable);
}
