package com.decormoi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.decormoi.app.IntegrationTest;
import com.decormoi.app.domain.CategorieProduit;
import com.decormoi.app.repository.CategorieProduitRepository;
import com.decormoi.app.service.criteria.CategorieProduitCriteria;
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
 * Integration tests for the {@link CategorieProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorieProduitResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorie-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieProduitRepository categorieProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieProduitMockMvc;

    private CategorieProduit categorieProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieProduit createEntity(EntityManager em) {
        CategorieProduit categorieProduit = new CategorieProduit().nom(DEFAULT_NOM);
        return categorieProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieProduit createUpdatedEntity(EntityManager em) {
        CategorieProduit categorieProduit = new CategorieProduit().nom(UPDATED_NOM);
        return categorieProduit;
    }

    @BeforeEach
    public void initTest() {
        categorieProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createCategorieProduit() throws Exception {
        int databaseSizeBeforeCreate = categorieProduitRepository.findAll().size();
        // Create the CategorieProduit
        restCategorieProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isCreated());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createCategorieProduitWithExistingId() throws Exception {
        // Create the CategorieProduit with an existing ID
        categorieProduit.setId(1L);

        int databaseSizeBeforeCreate = categorieProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieProduitRepository.findAll().size();
        // set the field null
        categorieProduit.setNom(null);

        // Create the CategorieProduit, which fails.

        restCategorieProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorieProduits() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get the categorieProduit
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, categorieProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorieProduit.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getCategorieProduitsByIdFiltering() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        Long id = categorieProduit.getId();

        defaultCategorieProduitShouldBeFound("id.equals=" + id);
        defaultCategorieProduitShouldNotBeFound("id.notEquals=" + id);

        defaultCategorieProduitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategorieProduitShouldNotBeFound("id.greaterThan=" + id);

        defaultCategorieProduitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategorieProduitShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom equals to DEFAULT_NOM
        defaultCategorieProduitShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the categorieProduitList where nom equals to UPDATED_NOM
        defaultCategorieProduitShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom not equals to DEFAULT_NOM
        defaultCategorieProduitShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the categorieProduitList where nom not equals to UPDATED_NOM
        defaultCategorieProduitShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCategorieProduitShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the categorieProduitList where nom equals to UPDATED_NOM
        defaultCategorieProduitShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom is not null
        defaultCategorieProduitShouldBeFound("nom.specified=true");

        // Get all the categorieProduitList where nom is null
        defaultCategorieProduitShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomContainsSomething() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom contains DEFAULT_NOM
        defaultCategorieProduitShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the categorieProduitList where nom contains UPDATED_NOM
        defaultCategorieProduitShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCategorieProduitsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList where nom does not contain DEFAULT_NOM
        defaultCategorieProduitShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the categorieProduitList where nom does not contain UPDATED_NOM
        defaultCategorieProduitShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategorieProduitShouldBeFound(String filter) throws Exception {
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategorieProduitShouldNotBeFound(String filter) throws Exception {
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategorieProduit() throws Exception {
        // Get the categorieProduit
        restCategorieProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit
        CategorieProduit updatedCategorieProduit = categorieProduitRepository.findById(categorieProduit.getId()).get();
        // Disconnect from session so that the updates on updatedCategorieProduit are not directly saved in db
        em.detach(updatedCategorieProduit);
        updatedCategorieProduit.nom(UPDATED_NOM);

        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategorieProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorieProduitWithPatch() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit using partial update
        CategorieProduit partialUpdatedCategorieProduit = new CategorieProduit();
        partialUpdatedCategorieProduit.setId(categorieProduit.getId());

        partialUpdatedCategorieProduit.nom(UPDATED_NOM);

        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCategorieProduitWithPatch() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit using partial update
        CategorieProduit partialUpdatedCategorieProduit = new CategorieProduit();
        partialUpdatedCategorieProduit.setId(categorieProduit.getId());

        partialUpdatedCategorieProduit.nom(UPDATED_NOM);

        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorieProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeDelete = categorieProduitRepository.findAll().size();

        // Delete the categorieProduit
        restCategorieProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorieProduit.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
