package com.elishajohnson.powerattack.repository;

import com.elishajohnson.powerattack.domain.Character;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Character entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("select character from Character character where character.user.login = ?#{principal.username}")
    List<Character> findByUserIsCurrentUser();

}
