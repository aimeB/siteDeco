package com.decormoi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.decormoi.app.IntegrationTest;
import com.decormoi.app.domain.TypeEvenement;
import com.decormoi.app.repository.TypeEvenementRepository;
import com.decormoi.app.service.criteria.TypeEvenementCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeEvenementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeEvenementResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-evenements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeEvenementRepository typeEvenementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeEvenementMockMvc;

    private TypeEvenement typeEvenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeEvenement createEntity(EntityManager em) {
        TypeEvenement typeEvenement = new TypeEvenement().nom(DEFAULT_NOM);
        return typeEvenement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeEvenement createUpdatedEntity(EntityManager em) {
        TypeEvenement typeEvenement = new TypeEvenement().nom(UPDATED_NOM);
        return typeEvenement;
    }

    @BeforeEach
    public void initTest() {
        typeEvenement = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeEvenement() throws Exception {
        int databaseSizeBeforeCreate = typeEvenementRepository.findAll().size();
        // Create the TypeEvenement
        restTypeEvenementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isCreated());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeCreate + 1);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createTypeEvenementWithExistingId() throws Exception {
        // Create the TypeEvenement with an existing ID
        typeEvenement.setId(1L);

        int databaseSizeBeforeCreate = typeEvenementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeEvenementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeEvenementRepository.findAll().size();
        // set the field null
        typeEvenement.setNom(null);

        // Create the TypeEvenement, which fails.

        restTypeEvenementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeEvenements() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get the typeEvenement
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL_ID, typeEvenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeEvenement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getTypeEvenementsByIdFiltering() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        Long id = typeEvenement.getId();

        defaultTypeEvenementShouldBeFound("id.equals=" + id);
        defaultTypeEvenementShouldNotBeFound("id.notEquals=" + id);

        defaultTypeEvenementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeEvenementShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeEvenementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeEvenementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom equals to DEFAULT_NOM
        defaultTypeEvenementShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the typeEvenementList where nom equals to UPDATED_NOM
        defaultTypeEvenementShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom not equals to DEFAULT_NOM
        defaultTypeEvenementShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the typeEvenementList where nom not equals to UPDATED_NOM
        defaultTypeEvenementShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultTypeEvenementShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the typeEvenementList where nom equals to UPDATED_NOM
        defaultTypeEvenementShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom is not null
        defaultTypeEvenementShouldBeFound("nom.specified=true");

        // Get all the typeEvenementList where nom is null
        defaultTypeEvenementShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom contains DEFAULT_NOM
        defaultTypeEvenementShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the typeEvenementList where nom contains UPDATED_NOM
        defaultTypeEvenementShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where nom does not contain DEFAULT_NOM
        defaultTypeEvenementShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the typeEvenementList where nom does not contain UPDATED_NOM
        defaultTypeEvenementShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeEvenementShouldBeFound(String filter) throws Exception {
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeEvenementShouldNotBeFound(String filter) throws Exception {
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeEvenement() throws Exception {
        // Get the typeEvenement
        restTypeEvenementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement
        TypeEvenement updatedTypeEvenement = typeEvenementRepository.findById(typeEvenement.getId()).get();
        // Disconnect from session so that the updates on updatedTypeEvenement are not directly saved in db
        em.detach(updatedTypeEvenement);
        updatedTypeEvenement.nom(UPDATED_NOM);

        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeEvenement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeEvenement))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeEvenement.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeEvenementWithPatch() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement using partial update
        TypeEvenement partialUpdatedTypeEvenement = new TypeEvenement();
        partialUpdatedTypeEvenement.setId(typeEvenement.getId());

        partialUpdatedTypeEvenement.nom(UPDATED_NOM);

        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeEvenement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeEvenement))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateTypeEvenementWithPatch() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement using partial update
        TypeEvenement partialUpdatedTypeEvenement = new TypeEvenement();
        partialUpdatedTypeEvenement.setId(typeEvenement.getId());

        partialUpdatedTypeEvenement.nom(UPDATED_NOM);

        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeEvenement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeEvenement))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeEvenement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeDelete = typeEvenementRepository.findAll().size();

        // Delete the typeEvenement
        restTypeEvenementMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeEvenement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
