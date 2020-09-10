package com.elishajohnson.powerattack.repository;

import com.elishajohnson.powerattack.domain.AttackCycle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AttackCycle entity.
 */
@Repository
public interface AttackCycleRepository extends JpaRepository<AttackCycle, Long> {

    @Query(value = "select distinct attackCycle from AttackCycle attackCycle left join fetch attackCycle.attacks",
        countQuery = "select count(distinct attackCycle) from AttackCycle attackCycle")
    Page<AttackCycle> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct attackCycle from AttackCycle attackCycle left join fetch attackCycle.attacks")
    List<AttackCycle> findAllWithEagerRelationships();

    @Query("select attackCycle from AttackCycle attackCycle left join fetch attackCycle.attacks where attackCycle.id =:id")
    Optional<AttackCycle> findOneWithEagerRelationships(@Param("id") Long id);

}
