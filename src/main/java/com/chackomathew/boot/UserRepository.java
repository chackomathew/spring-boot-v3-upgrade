package com.chackomathew.boot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByDeletedFalse();

    public List<User> findByAdminTrue();

    @Query("select u from User u where u.deleted = false")
    public List<User> findByDeletedFalseQuery();

    @Query("select u from User u where u.admin = true")
    public List<User> findByAdminTrueQuery();
}
