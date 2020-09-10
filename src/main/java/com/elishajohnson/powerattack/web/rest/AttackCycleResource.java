package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.domain.AttackCycle;
import com.elishajohnson.powerattack.repository.AttackCycleRepository;
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
 * REST controller for managing {@link com.elishajohnson.powerattack.domain.AttackCycle}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttackCycleResource {

    private final Logger log = LoggerFactory.getLogger(AttackCycleResource.class);

    private static final String ENTITY_NAME = "attackCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttackCycleRepository attackCycleRepository;

    public AttackCycleResource(AttackCycleRepository attackCycleRepository) {
        this.attackCycleRepository = attackCycleRepository;
    }

    /**
     * {@code POST  /attack-cycles} : Create a new attackCycle.
     *
     * @param attackCycle the attackCycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attackCycle, or with status {@code 400 (Bad Request)} if the attackCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attack-cycles")
    public ResponseEntity<AttackCycle> createAttackCycle(@Valid @RequestBody AttackCycle attackCycle) throws URISyntaxException {
        log.debug("REST request to save AttackCycle : {}", attackCycle);
        if (attackCycle.getId() != null) {
            throw new BadRequestAlertException("A new attackCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttackCycle result = attackCycleRepository.save(attackCycle);
        return ResponseEntity.created(new URI("/api/attack-cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attack-cycles} : Updates an existing attackCycle.
     *
     * @param attackCycle the attackCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attackCycle,
     * or with status {@code 400 (Bad Request)} if the attackCycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attackCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attack-cycles")
    public ResponseEntity<AttackCycle> updateAttackCycle(@Valid @RequestBody AttackCycle attackCycle) throws URISyntaxException {
        log.debug("REST request to update AttackCycle : {}", attackCycle);
        if (attackCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttackCycle result = attackCycleRepository.save(attackCycle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attackCycle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attack-cycles} : get all the attackCycles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attackCycles in body.
     */
    @GetMapping("/attack-cycles")
    public List<AttackCycle> getAllAttackCycles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all AttackCycles");
        return attackCycleRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /attack-cycles/:id} : get the "id" attackCycle.
     *
     * @param id the id of the attackCycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attackCycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attack-cycles/{id}")
    public ResponseEntity<AttackCycle> getAttackCycle(@PathVariable Long id) {
        log.debug("REST request to get AttackCycle : {}", id);
        Optional<AttackCycle> attackCycle = attackCycleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(attackCycle);
    }

    /**
     * {@code DELETE  /attack-cycles/:id} : delete the "id" attackCycle.
     *
     * @param id the id of the attackCycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attack-cycles/{id}")
    public ResponseEntity<Void> deleteAttackCycle(@PathVariable Long id) {
        log.debug("REST request to delete AttackCycle : {}", id);
        attackCycleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
