package com.utez.sda.springjwt.repository;

import com.utez.sda.springjwt.model.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    public Optional<UserInfo> findByUsername(String username);
}
