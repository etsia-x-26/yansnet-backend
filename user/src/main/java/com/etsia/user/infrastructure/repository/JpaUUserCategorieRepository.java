package com.etsia.user.infrastructure.repository;

import com.etsia.common.infrastructure.entities.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "UCJRepository")
public interface JpaUUserCategorieRepository extends JpaRepository<UserCategory, Integer> {

}
