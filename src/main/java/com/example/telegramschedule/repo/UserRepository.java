package com.example.telegramschedule.repo;

import com.example.telegramschedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    @Override
    User getOne(Long aLong);
}
