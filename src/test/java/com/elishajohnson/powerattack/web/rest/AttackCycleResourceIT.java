package com.elishajohnson.powerattack.web.rest;

import com.elishajohnson.powerattack.PowerattackApp;
import com.elishajohnson.powerattack.domain.AttackCycle;
import com.elishajohnson.powerattack.repository.AttackCycleRepository;
import com.elishajohnson.powerattack.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.elishajohnson.powerattack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AttackCycleResource} REST controller.
 */
@SpringBootTest(classes = PowerattackApp.class)
public class AttackCycleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AttackCycleRepository attackCycleRepository;

    @Mock
    private AttackCycleRepository attackCycleRepositoryMock;

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

    private MockMvc restAttackCycleMockMvc;

    private AttackCycle attackCycle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttackCycleResource attackCycleResource = new AttackCycleResource(attackCycleRepository);
        this.restAttackCycleMockMvc = MockMvcBuilders.standaloneSetup(attackCycleResource)
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
    public static AttackCycle createEntity(EntityManager em) {
        AttackCycle attackCycle = new AttackCycle()
            .name(DEFAULT_NAME);
        return attackCycle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttackCycle createUpdatedEntity(EntityManager em) {
        AttackCycle attackCycle = new AttackCycle()
            .name(UPDATED_NAME);
        return attackCycle;
    }

    @BeforeEach
    public void initTest() {
        attackCycle = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttackCycle() throws Exception {
        int databaseSizeBeforeCreate = attackCycleRepository.findAll().size();

        // Create the AttackCycle
        restAttackCycleMockMvc.perform(post("/api/attack-cycles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attackCycle)))
            .andExpect(status().isCreated());

        // Validate the AttackCycle in the database
        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeCreate + 1);
        AttackCycle testAttackCycle = attackCycleList.get(attackCycleList.size() - 1);
        assertThat(testAttackCycle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAttackCycleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attackCycleRepository.findAll().size();

        // Create the AttackCycle with an existing ID
        attackCycle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttackCycleMockMvc.perform(post("/api/attack-cycles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attackCycle)))
            .andExpect(status().isBadRequest());

        // Validate the AttackCycle in the database
        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attackCycleRepository.findAll().size();
        // set the field null
        attackCycle.setName(null);

        // Create the AttackCycle, which fails.

        restAttackCycleMockMvc.perform(post("/api/attack-cycles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attackCycle)))
            .andExpect(status().isBadRequest());

        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttackCycles() throws Exception {
        // Initialize the database
        attackCycleRepository.saveAndFlush(attackCycle);

        // Get all the attackCycleList
        restAttackCycleMockMvc.perform(get("/api/attack-cycles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attackCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllAttackCyclesWithEagerRelationshipsIsEnabled() throws Exception {
        AttackCycleResource attackCycleResource = new AttackCycleResource(attackCycleRepositoryMock);
        when(attackCycleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restAttackCycleMockMvc = MockMvcBuilders.standaloneSetup(attackCycleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAttackCycleMockMvc.perform(get("/api/attack-cycles?eagerload=true"))
        .andExpect(status().isOk());

        verify(attackCycleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllAttackCyclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        AttackCycleResource attackCycleResource = new AttackCycleResource(attackCycleRepositoryMock);
            when(attackCycleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restAttackCycleMockMvc = MockMvcBuilders.standaloneSetup(attackCycleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAttackCycleMockMvc.perform(get("/api/attack-cycles?eagerload=true"))
        .andExpect(status().isOk());

            verify(attackCycleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAttackCycle() throws Exception {
        // Initialize the database
        attackCycleRepository.saveAndFlush(attackCycle);

        // Get the attackCycle
        restAttackCycleMockMvc.perform(get("/api/attack-cycles/{id}", attackCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attackCycle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingAttackCycle() throws Exception {
        // Get the attackCycle
        restAttackCycleMockMvc.perform(get("/api/attack-cycles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttackCycle() throws Exception {
        // Initialize the database
        attackCycleRepository.saveAndFlush(attackCycle);

        int databaseSizeBeforeUpdate = attackCycleRepository.findAll().size();

        // Update the attackCycle
        AttackCycle updatedAttackCycle = attackCycleRepository.findById(attackCycle.getId()).get();
        // Disconnect from session so that the updates on updatedAttackCycle are not directly saved in db
        em.detach(updatedAttackCycle);
        updatedAttackCycle
            .name(UPDATED_NAME);

        restAttackCycleMockMvc.perform(put("/api/attack-cycles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttackCycle)))
            .andExpect(status().isOk());

        // Validate the AttackCycle in the database
        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeUpdate);
        AttackCycle testAttackCycle = attackCycleList.get(attackCycleList.size() - 1);
        assertThat(testAttackCycle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAttackCycle() throws Exception {
        int databaseSizeBeforeUpdate = attackCycleRepository.findAll().size();

        // Create the AttackCycle

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttackCycleMockMvc.perform(put("/api/attack-cycles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attackCycle)))
            .andExpect(status().isBadRequest());

        // Validate the AttackCycle in the database
        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttackCycle() throws Exception {
        // Initialize the database
        attackCycleRepository.saveAndFlush(attackCycle);

        int databaseSizeBeforeDelete = attackCycleRepository.findAll().size();

        // Delete the attackCycle
        restAttackCycleMockMvc.perform(delete("/api/attack-cycles/{id}", attackCycle.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttackCycle> attackCycleList = attackCycleRepository.findAll();
        assertThat(attackCycleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
