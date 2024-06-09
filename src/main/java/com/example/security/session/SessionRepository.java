package com.example.security.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session,Integer> {
    Optional<Session> findByUserId(Integer userId);

    void deleteByUserId(Integer userId);
}
