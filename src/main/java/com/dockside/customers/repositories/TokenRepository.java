package com.dockside.customers.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dockside.customers.Domain.Authentication.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
    
    Optional<Token> findByToken(String token);
    
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.userid = u.userid\s
            where userid = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokensByUser(UUID id);
} 
