package com.example.spring.batch.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.spring.batch.entity.Profile;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

}