package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.domain.Attack;
import com.elishajohnson.powerattack.repository.AttackRepository;
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
 * REST controller for managing {@link com.elishajohnson.powerattack.domain.Attack}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttackResource {

    private final Logger log = LoggerFactory.getLogger(AttackResource.class);

    private static final String ENTITY_NAME = "attack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttackRepository attackRepository;

    public AttackResource(AttackRepository attackRepository) {
        this.attackRepository = attackRepository;
    }

    /**
     * {@code POST  /attacks} : Create a new attack.
     *
     * @param attack the attack to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attack, or with status {@code 400 (Bad Request)} if the attack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attacks")
    public ResponseEntity<Attack> createAttack(@Valid @RequestBody Attack attack) throws URISyntaxException {
        log.debug("REST request to save Attack : {}", attack);
        if (attack.getId() != null) {
            throw new BadRequestAlertException("A new attack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attack result = attackRepository.save(attack);
        return ResponseEntity.created(new URI("/api/attacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attacks} : Updates an existing attack.
     *
     * @param attack the attack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attack,
     * or with status {@code 400 (Bad Request)} if the attack is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attacks")
    public ResponseEntity<Attack> updateAttack(@Valid @RequestBody Attack attack) throws URISyntaxException {
        log.debug("REST request to update Attack : {}", attack);
        if (attack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Attack result = attackRepository.save(attack);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attack.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attacks} : get all the attacks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attacks in body.
     */
    @GetMapping("/attacks")
    public List<Attack> getAllAttacks() {
        log.debug("REST request to get all Attacks");
        return attackRepository.findAll();
    }

    /**
     * {@code GET  /attacks/:id} : get the "id" attack.
     *
     * @param id the id of the attack to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attack, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attacks/{id}")
    public ResponseEntity<Attack> getAttack(@PathVariable Long id) {
        log.debug("REST request to get Attack : {}", id);
        Optional<Attack> attack = attackRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attack);
    }

    /**
     * {@code DELETE  /attacks/:id} : delete the "id" attack.
     *
     * @param id the id of the attack to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attacks/{id}")
    public ResponseEntity<Void> deleteAttack(@PathVariable Long id) {
        log.debug("REST request to delete Attack : {}", id);
        attackRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
