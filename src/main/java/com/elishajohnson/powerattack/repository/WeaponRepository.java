package com.elishajohnson.powerattack.repository;

import com.elishajohnson.powerattack.domain.Weapon;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Weapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {

}
