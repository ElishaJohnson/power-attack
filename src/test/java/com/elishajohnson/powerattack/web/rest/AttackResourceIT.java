package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.PowerattackApp;
import com.elishajohnson.powerattack.domain.Attack;
import com.elishajohnson.powerattack.repository.AttackRepository;
import com.elishajohnson.powerattack.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.elishajohnson.powerattack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AttackResource} REST controller.
 */
@SpringBootTest(classes = PowerattackApp.class)
public class AttackResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTACK_MODIFIER = 100;
    private static final Integer UPDATED_ATTACK_MODIFIER = 99;

    private static final Integer DEFAULT_CRIT_CHANCE = 100;
    private static final Integer UPDATED_CRIT_CHANCE = 99;

    private static final Integer DEFAULT_CRIT_DAMAGE = 100;
    private static final Integer UPDATED_CRIT_DAMAGE = 99;

    private static final Integer DEFAULT_DIE_VALUE = 100;
    private static final Integer UPDATED_DIE_VALUE = 99;

    private static final Integer DEFAULT_DICE_COUNT = 100;
    private static final Integer UPDATED_DICE_COUNT = 99;

    private static final Integer DEFAULT_DAMAGE_BONUS = 100;
    private static final Integer UPDATED_DAMAGE_BONUS = 99;

    @Autowired
    private AttackRepository attackRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAttackMockMvc;

    private Attack attack;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttackResource attackResource = new AttackResource(attackRepository);
        this.restAttackMockMvc = MockMvcBuilders.standaloneSetup(attackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attack createEntity(EntityManager em) {
        Attack attack = new Attack()
            .name(DEFAULT_NAME)
            .attackModifier(DEFAULT_ATTACK_MODIFIER)
            .critChance(DEFAULT_CRIT_CHANCE)
            .critDamage(DEFAULT_CRIT_DAMAGE)
            .dieValue(DEFAULT_DIE_VALUE)
            .diceCount(DEFAULT_DICE_COUNT)
            .damageBonus(DEFAULT_DAMAGE_BONUS);
        return attack;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attack createUpdatedEntity(EntityManager em) {
        Attack attack = new Attack()
            .name(UPDATED_NAME)
            .attackModifier(UPDATED_ATTACK_MODIFIER)
            .critChance(UPDATED_CRIT_CHANCE)
            .critDamage(UPDATED_CRIT_DAMAGE)
            .dieValue(UPDATED_DIE_VALUE)
            .diceCount(UPDATED_DICE_COUNT)
            .damageBonus(UPDATED_DAMAGE_BONUS);
        return attack;
    }

    @BeforeEach
    public void initTest() {
        attack = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttack() throws Exception {
        int databaseSizeBeforeCreate = attackRepository.findAll().size();

        // Create the Attack
        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isCreated());

        // Validate the Attack in the database
        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeCreate + 1);
        Attack testAttack = attackList.get(attackList.size() - 1);
        assertThat(testAttack.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttack.getAttackModifier()).isEqualTo(DEFAULT_ATTACK_MODIFIER);
        assertThat(testAttack.getCritChance()).isEqualTo(DEFAULT_CRIT_CHANCE);
        assertThat(testAttack.getCritDamage()).isEqualTo(DEFAULT_CRIT_DAMAGE);
        assertThat(testAttack.getDieValue()).isEqualTo(DEFAULT_DIE_VALUE);
        assertThat(testAttack.getDiceCount()).isEqualTo(DEFAULT_DICE_COUNT);
        assertThat(testAttack.getDamageBonus()).isEqualTo(DEFAULT_DAMAGE_BONUS);
    }

    @Test
    @Transactional
    public void createAttackWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attackRepository.findAll().size();

        // Create the Attack with an existing ID
        attack.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        // Validate the Attack in the database
        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setName(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackModifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setAttackModifier(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCritChanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setCritChance(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCritDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setCritDamage(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDieValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setDieValue(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiceCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setDiceCount(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageBonusIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackRepository.findAll().size();
        // set the field null
        attack.setDamageBonus(null);

        // Create the Attack, which fails.

        restAttackMockMvc.perform(post("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttacks() throws Exception {
        // Initialize the database
        attackRepository.saveAndFlush(attack);

        // Get all the attackList
        restAttackMockMvc.perform(get("/api/attacks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attack.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].attackModifier").value(hasItem(DEFAULT_ATTACK_MODIFIER)))
            .andExpect(jsonPath("$.[*].critChance").value(hasItem(DEFAULT_CRIT_CHANCE)))
            .andExpect(jsonPath("$.[*].critDamage").value(hasItem(DEFAULT_CRIT_DAMAGE)))
            .andExpect(jsonPath("$.[*].dieValue").value(hasItem(DEFAULT_DIE_VALUE)))
            .andExpect(jsonPath("$.[*].diceCount").value(hasItem(DEFAULT_DICE_COUNT)))
            .andExpect(jsonPath("$.[*].damageBonus").value(hasItem(DEFAULT_DAMAGE_BONUS)));
    }
    
    @Test
    @Transactional
    public void getAttack() throws Exception {
        // Initialize the database
        attackRepository.saveAndFlush(attack);

        // Get the attack
        restAttackMockMvc.perform(get("/api/attacks/{id}", attack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attack.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.attackModifier").value(DEFAULT_ATTACK_MODIFIER))
            .andExpect(jsonPath("$.critChance").value(DEFAULT_CRIT_CHANCE))
            .andExpect(jsonPath("$.critDamage").value(DEFAULT_CRIT_DAMAGE))
            .andExpect(jsonPath("$.dieValue").value(DEFAULT_DIE_VALUE))
            .andExpect(jsonPath("$.diceCount").value(DEFAULT_DICE_COUNT))
            .andExpect(jsonPath("$.damageBonus").value(DEFAULT_DAMAGE_BONUS));
    }

    @Test
    @Transactional
    public void getNonExistingAttack() throws Exception {
        // Get the attack
        restAttackMockMvc.perform(get("/api/attacks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttack() throws Exception {
        // Initialize the database
        attackRepository.saveAndFlush(attack);

        int databaseSizeBeforeUpdate = attackRepository.findAll().size();

        // Update the attack
        Attack updatedAttack = attackRepository.findById(attack.getId()).get();
        // Disconnect from session so that the updates on updatedAttack are not directly saved in db
        em.detach(updatedAttack);
        updatedAttack
            .name(UPDATED_NAME)
            .attackModifier(UPDATED_ATTACK_MODIFIER)
            .critChance(UPDATED_CRIT_CHANCE)
            .critDamage(UPDATED_CRIT_DAMAGE)
            .dieValue(UPDATED_DIE_VALUE)
            .diceCount(UPDATED_DICE_COUNT)
            .damageBonus(UPDATED_DAMAGE_BONUS);

        restAttackMockMvc.perform(put("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttack)))
            .andExpect(status().isOk());

        // Validate the Attack in the database
        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeUpdate);
        Attack testAttack = attackList.get(attackList.size() - 1);
        assertThat(testAttack.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttack.getAttackModifier()).isEqualTo(UPDATED_ATTACK_MODIFIER);
        assertThat(testAttack.getCritChance()).isEqualTo(UPDATED_CRIT_CHANCE);
        assertThat(testAttack.getCritDamage()).isEqualTo(UPDATED_CRIT_DAMAGE);
        assertThat(testAttack.getDieValue()).isEqualTo(UPDATED_DIE_VALUE);
        assertThat(testAttack.getDiceCount()).isEqualTo(UPDATED_DICE_COUNT);
        assertThat(testAttack.getDamageBonus()).isEqualTo(UPDATED_DAMAGE_BONUS);
    }

    @Test
    @Transactional
    public void updateNonExistingAttack() throws Exception {
        int databaseSizeBeforeUpdate = attackRepository.findAll().size();

        // Create the Attack

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttackMockMvc.perform(put("/api/attacks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attack)))
            .andExpect(status().isBadRequest());

        // Validate the Attack in the database
        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttack() throws Exception {
        // Initialize the database
        attackRepository.saveAndFlush(attack);

        int databaseSizeBeforeDelete = attackRepository.findAll().size();

        // Delete the attack
        restAttackMockMvc.perform(delete("/api/attacks/{id}", attack.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attack> attackList = attackRepository.findAll();
        assertThat(attackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
