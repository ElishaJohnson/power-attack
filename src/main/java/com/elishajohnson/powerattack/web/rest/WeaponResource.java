package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.domain.Weapon;
import com.elishajohnson.powerattack.repository.WeaponRepository;
import com.elishajohnson.powerattack.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.elishajohnson.powerattack.domain.Weapon}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WeaponResource {

    private final Logger log = LoggerFactory.getLogger(WeaponResource.class);

    private static final String ENTITY_NAME = "weapon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeaponRepository weaponRepository;

    public WeaponResource(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    /**
     * {@code POST  /weapons} : Create a new weapon.
     *
     * @param weapon the weapon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weapon, or with status {@code 400 (Bad Request)} if the weapon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weapons")
    public ResponseEntity<Weapon> createWeapon(@Valid @RequestBody Weapon weapon) throws URISyntaxException {
        log.debug("REST request to save Weapon : {}", weapon);
        if (weapon.getId() != null) {
            throw new BadRequestAlertException("A new weapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Weapon result = weaponRepository.save(weapon);
        return ResponseEntity.created(new URI("/api/weapons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weapons} : Updates an existing weapon.
     *
     * @param weapon the weapon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weapon,
     * or with status {@code 400 (Bad Request)} if the weapon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weapon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weapons")
    public ResponseEntity<Weapon> updateWeapon(@Valid @RequestBody Weapon weapon) throws URISyntaxException {
        log.debug("REST request to update Weapon : {}", weapon);
        if (weapon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Weapon result = weaponRepository.save(weapon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weapon.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /weapons} : get all the weapons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weapons in body.
     */
    @GetMapping("/weapons")
    public List<Weapon> getAllWeapons() {
        log.debug("REST request to get all Weapons");
        return weaponRepository.findAll();
    }

    /**
     * {@code GET  /weapons/:id} : get the "id" weapon.
     *
     * @param id the id of the weapon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weapon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weapons/{id}")
    public ResponseEntity<Weapon> getWeapon(@PathVariable Long id) {
        log.debug("REST request to get Weapon : {}", id);
        Optional<Weapon> weapon = weaponRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weapon);
    }

    /**
     * {@code DELETE  /weapons/:id} : delete the "id" weapon.
     *
     * @param id the id of the weapon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weapons/{id}")
    public ResponseEntity<Void> deleteWeapon(@PathVariable Long id) {
        log.debug("REST request to delete Weapon : {}", id);
        weaponRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
