package com.elishajohnson.powerattack.repository;

import com.elishajohnson.powerattack.domain.Attack;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Attack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttackRepository extends JpaRepository<Attack, Long> {

}
