package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.PowerattackApp;
import com.elishajohnson.powerattack.domain.Weapon;
import com.elishajohnson.powerattack.repository.WeaponRepository;
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
 * Integration tests for the {@link WeaponResource} REST controller.
 */
@SpringBootTest(classes = PowerattackApp.class)
public class WeaponResourceIT {

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
    private WeaponRepository weaponRepository;

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

    private MockMvc restWeaponMockMvc;

    private Weapon weapon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeaponResource weaponResource = new WeaponResource(weaponRepository);
        this.restWeaponMockMvc = MockMvcBuilders.standaloneSetup(weaponResource)
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
    public static Weapon createEntity(EntityManager em) {
        Weapon weapon = new Weapon()
            .name(DEFAULT_NAME)
            .attackModifier(DEFAULT_ATTACK_MODIFIER)
            .critChance(DEFAULT_CRIT_CHANCE)
            .critDamage(DEFAULT_CRIT_DAMAGE)
            .dieValue(DEFAULT_DIE_VALUE)
            .diceCount(DEFAULT_DICE_COUNT)
            .damageBonus(DEFAULT_DAMAGE_BONUS);
        return weapon;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weapon createUpdatedEntity(EntityManager em) {
        Weapon weapon = new Weapon()
            .name(UPDATED_NAME)
            .attackModifier(UPDATED_ATTACK_MODIFIER)
            .critChance(UPDATED_CRIT_CHANCE)
            .critDamage(UPDATED_CRIT_DAMAGE)
            .dieValue(UPDATED_DIE_VALUE)
            .diceCount(UPDATED_DICE_COUNT)
            .damageBonus(UPDATED_DAMAGE_BONUS);
        return weapon;
    }

    @BeforeEach
    public void initTest() {
        weapon = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeapon() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().size();

        // Create the Weapon
        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isCreated());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate + 1);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWeapon.getAttackModifier()).isEqualTo(DEFAULT_ATTACK_MODIFIER);
        assertThat(testWeapon.getCritChance()).isEqualTo(DEFAULT_CRIT_CHANCE);
        assertThat(testWeapon.getCritDamage()).isEqualTo(DEFAULT_CRIT_DAMAGE);
        assertThat(testWeapon.getDieValue()).isEqualTo(DEFAULT_DIE_VALUE);
        assertThat(testWeapon.getDiceCount()).isEqualTo(DEFAULT_DICE_COUNT);
        assertThat(testWeapon.getDamageBonus()).isEqualTo(DEFAULT_DAMAGE_BONUS);
    }

    @Test
    @Transactional
    public void createWeaponWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().size();

        // Create the Weapon with an existing ID
        weapon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setName(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackModifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setAttackModifier(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCritChanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setCritChance(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCritDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setCritDamage(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDieValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setDieValue(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiceCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setDiceCount(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageBonusIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setDamageBonus(null);

        // Create the Weapon, which fails.

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeapons() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get all the weaponList
        restWeaponMockMvc.perform(get("/api/weapons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weapon.getId().intValue())))
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
    public void getWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get the weapon
        restWeaponMockMvc.perform(get("/api/weapons/{id}", weapon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weapon.getId().intValue()))
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
    public void getNonExistingWeapon() throws Exception {
        // Get the weapon
        restWeaponMockMvc.perform(get("/api/weapons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Update the weapon
        Weapon updatedWeapon = weaponRepository.findById(weapon.getId()).get();
        // Disconnect from session so that the updates on updatedWeapon are not directly saved in db
        em.detach(updatedWeapon);
        updatedWeapon
            .name(UPDATED_NAME)
            .attackModifier(UPDATED_ATTACK_MODIFIER)
            .critChance(UPDATED_CRIT_CHANCE)
            .critDamage(UPDATED_CRIT_DAMAGE)
            .dieValue(UPDATED_DIE_VALUE)
            .diceCount(UPDATED_DICE_COUNT)
            .damageBonus(UPDATED_DAMAGE_BONUS);

        restWeaponMockMvc.perform(put("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWeapon)))
            .andExpect(status().isOk());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getAttackModifier()).isEqualTo(UPDATED_ATTACK_MODIFIER);
        assertThat(testWeapon.getCritChance()).isEqualTo(UPDATED_CRIT_CHANCE);
        assertThat(testWeapon.getCritDamage()).isEqualTo(UPDATED_CRIT_DAMAGE);
        assertThat(testWeapon.getDieValue()).isEqualTo(UPDATED_DIE_VALUE);
        assertThat(testWeapon.getDiceCount()).isEqualTo(UPDATED_DICE_COUNT);
        assertThat(testWeapon.getDamageBonus()).isEqualTo(UPDATED_DAMAGE_BONUS);
    }

    @Test
    @Transactional
    public void updateNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Create the Weapon

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponMockMvc.perform(put("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weapon)))
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeDelete = weaponRepository.findAll().size();

        // Delete the weapon
        restWeaponMockMvc.perform(delete("/api/weapons/{id}", weapon.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
