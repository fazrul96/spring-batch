package com.example.spring.batch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.example.spring.batch.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findByStatusAndEmailVerified(String status, boolean emailVerified, Pageable pageable);
}
