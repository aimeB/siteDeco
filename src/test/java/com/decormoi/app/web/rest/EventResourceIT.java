package com.decormoi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.decormoi.app.IntegrationTest;
import com.decormoi.app.domain.Event;
import com.decormoi.app.domain.Produit;
import com.decormoi.app.domain.Salle;
import com.decormoi.app.domain.TypeEvenement;
import com.decormoi.app.domain.User;
import com.decormoi.app.repository.EventRepository;
import com.decormoi.app.service.EventService;
import com.decormoi.app.service.criteria.EventCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_EVENEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EVENEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;
    private static final Double SMALLER_PRIX = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Mock
    private EventRepository eventRepositoryMock;

    @Mock
    private EventService eventServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event().nom(DEFAULT_NOM).dateEvenement(DEFAULT_DATE_EVENEMENT).prix(DEFAULT_PRIX);
        // Add required entity
        TypeEvenement typeEvenement;
        if (TestUtil.findAll(em, TypeEvenement.class).isEmpty()) {
            typeEvenement = TypeEvenementResourceIT.createEntity(em);
            em.persist(typeEvenement);
            em.flush();
        } else {
            typeEvenement = TestUtil.findAll(em, TypeEvenement.class).get(0);
        }
        event.setTypeEvenement(typeEvenement);
        // Add required entity
        Salle salle;
        if (TestUtil.findAll(em, Salle.class).isEmpty()) {
            salle = SalleResourceIT.createEntity(em);
            em.persist(salle);
            em.flush();
        } else {
            salle = TestUtil.findAll(em, Salle.class).get(0);
        }
        event.setSalle(salle);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event().nom(UPDATED_NOM).dateEvenement(UPDATED_DATE_EVENEMENT).prix(UPDATED_PRIX);
        // Add required entity
        TypeEvenement typeEvenement;
        if (TestUtil.findAll(em, TypeEvenement.class).isEmpty()) {
            typeEvenement = TypeEvenementResourceIT.createUpdatedEntity(em);
            em.persist(typeEvenement);
            em.flush();
        } else {
            typeEvenement = TestUtil.findAll(em, TypeEvenement.class).get(0);
        }
        event.setTypeEvenement(typeEvenement);
        // Add required entity
        Salle salle;
        if (TestUtil.findAll(em, Salle.class).isEmpty()) {
            salle = SalleResourceIT.createUpdatedEntity(em);
            em.persist(salle);
            em.flush();
        } else {
            salle = TestUtil.findAll(em, Salle.class).get(0);
        }
        event.setSalle(salle);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEvent.getDateEvenement()).isEqualTo(DEFAULT_DATE_EVENEMENT);
        assertThat(testEvent.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setNom(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEvenementIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setDateEvenement(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateEvenement").value(hasItem(DEFAULT_DATE_EVENEMENT.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventsWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateEvenement").value(DEFAULT_DATE_EVENEMENT.toString()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom equals to DEFAULT_NOM
        defaultEventShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the eventList where nom equals to UPDATED_NOM
        defaultEventShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEventsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom not equals to DEFAULT_NOM
        defaultEventShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the eventList where nom not equals to UPDATED_NOM
        defaultEventShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEventsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultEventShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the eventList where nom equals to UPDATED_NOM
        defaultEventShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEventsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom is not null
        defaultEventShouldBeFound("nom.specified=true");

        // Get all the eventList where nom is null
        defaultEventShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByNomContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom contains DEFAULT_NOM
        defaultEventShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the eventList where nom contains UPDATED_NOM
        defaultEventShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEventsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where nom does not contain DEFAULT_NOM
        defaultEventShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the eventList where nom does not contain UPDATED_NOM
        defaultEventShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEventsByDateEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateEvenement equals to DEFAULT_DATE_EVENEMENT
        defaultEventShouldBeFound("dateEvenement.equals=" + DEFAULT_DATE_EVENEMENT);

        // Get all the eventList where dateEvenement equals to UPDATED_DATE_EVENEMENT
        defaultEventShouldNotBeFound("dateEvenement.equals=" + UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllEventsByDateEvenementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateEvenement not equals to DEFAULT_DATE_EVENEMENT
        defaultEventShouldNotBeFound("dateEvenement.notEquals=" + DEFAULT_DATE_EVENEMENT);

        // Get all the eventList where dateEvenement not equals to UPDATED_DATE_EVENEMENT
        defaultEventShouldBeFound("dateEvenement.notEquals=" + UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllEventsByDateEvenementIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateEvenement in DEFAULT_DATE_EVENEMENT or UPDATED_DATE_EVENEMENT
        defaultEventShouldBeFound("dateEvenement.in=" + DEFAULT_DATE_EVENEMENT + "," + UPDATED_DATE_EVENEMENT);

        // Get all the eventList where dateEvenement equals to UPDATED_DATE_EVENEMENT
        defaultEventShouldNotBeFound("dateEvenement.in=" + UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllEventsByDateEvenementIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateEvenement is not null
        defaultEventShouldBeFound("dateEvenement.specified=true");

        // Get all the eventList where dateEvenement is null
        defaultEventShouldNotBeFound("dateEvenement.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix equals to DEFAULT_PRIX
        defaultEventShouldBeFound("prix.equals=" + DEFAULT_PRIX);

        // Get all the eventList where prix equals to UPDATED_PRIX
        defaultEventShouldNotBeFound("prix.equals=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix not equals to DEFAULT_PRIX
        defaultEventShouldNotBeFound("prix.notEquals=" + DEFAULT_PRIX);

        // Get all the eventList where prix not equals to UPDATED_PRIX
        defaultEventShouldBeFound("prix.notEquals=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix in DEFAULT_PRIX or UPDATED_PRIX
        defaultEventShouldBeFound("prix.in=" + DEFAULT_PRIX + "," + UPDATED_PRIX);

        // Get all the eventList where prix equals to UPDATED_PRIX
        defaultEventShouldNotBeFound("prix.in=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix is not null
        defaultEventShouldBeFound("prix.specified=true");

        // Get all the eventList where prix is null
        defaultEventShouldNotBeFound("prix.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix is greater than or equal to DEFAULT_PRIX
        defaultEventShouldBeFound("prix.greaterThanOrEqual=" + DEFAULT_PRIX);

        // Get all the eventList where prix is greater than or equal to UPDATED_PRIX
        defaultEventShouldNotBeFound("prix.greaterThanOrEqual=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix is less than or equal to DEFAULT_PRIX
        defaultEventShouldBeFound("prix.lessThanOrEqual=" + DEFAULT_PRIX);

        // Get all the eventList where prix is less than or equal to SMALLER_PRIX
        defaultEventShouldNotBeFound("prix.lessThanOrEqual=" + SMALLER_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix is less than DEFAULT_PRIX
        defaultEventShouldNotBeFound("prix.lessThan=" + DEFAULT_PRIX);

        // Get all the eventList where prix is less than UPDATED_PRIX
        defaultEventShouldBeFound("prix.lessThan=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByPrixIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where prix is greater than DEFAULT_PRIX
        defaultEventShouldNotBeFound("prix.greaterThan=" + DEFAULT_PRIX);

        // Get all the eventList where prix is greater than SMALLER_PRIX
        defaultEventShouldBeFound("prix.greaterThan=" + SMALLER_PRIX);
    }

    @Test
    @Transactional
    void getAllEventsByAppartenantAIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        User appartenantA = UserResourceIT.createEntity(em);
        em.persist(appartenantA);
        em.flush();
        event.setAppartenantA(appartenantA);
        eventRepository.saveAndFlush(event);
        Long appartenantAId = appartenantA.getId();

        // Get all the eventList where appartenantA equals to appartenantAId
        defaultEventShouldBeFound("appartenantAId.equals=" + appartenantAId);

        // Get all the eventList where appartenantA equals to (appartenantAId + 1)
        defaultEventShouldNotBeFound("appartenantAId.equals=" + (appartenantAId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByAgentEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        User agentEvenement = UserResourceIT.createEntity(em);
        em.persist(agentEvenement);
        em.flush();
        event.addAgentEvenement(agentEvenement);
        eventRepository.saveAndFlush(event);
        Long agentEvenementId = agentEvenement.getId();

        // Get all the eventList where agentEvenement equals to agentEvenementId
        defaultEventShouldBeFound("agentEvenementId.equals=" + agentEvenementId);

        // Get all the eventList where agentEvenement equals to (agentEvenementId + 1)
        defaultEventShouldNotBeFound("agentEvenementId.equals=" + (agentEvenementId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByTypeEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        TypeEvenement typeEvenement = TypeEvenementResourceIT.createEntity(em);
        em.persist(typeEvenement);
        em.flush();
        event.setTypeEvenement(typeEvenement);
        eventRepository.saveAndFlush(event);
        Long typeEvenementId = typeEvenement.getId();

        // Get all the eventList where typeEvenement equals to typeEvenementId
        defaultEventShouldBeFound("typeEvenementId.equals=" + typeEvenementId);

        // Get all the eventList where typeEvenement equals to (typeEvenementId + 1)
        defaultEventShouldNotBeFound("typeEvenementId.equals=" + (typeEvenementId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        event.addProduit(produit);
        eventRepository.saveAndFlush(event);
        Long produitId = produit.getId();

        // Get all the eventList where produit equals to produitId
        defaultEventShouldBeFound("produitId.equals=" + produitId);

        // Get all the eventList where produit equals to (produitId + 1)
        defaultEventShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }

    @Test
    @Transactional
    void getAllEventsBySalleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Salle salle = SalleResourceIT.createEntity(em);
        em.persist(salle);
        em.flush();
        event.setSalle(salle);
        eventRepository.saveAndFlush(event);
        Long salleId = salle.getId();

        // Get all the eventList where salle equals to salleId
        defaultEventShouldBeFound("salleId.equals=" + salleId);

        // Get all the eventList where salle equals to (salleId + 1)
        defaultEventShouldNotBeFound("salleId.equals=" + (salleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateEvenement").value(hasItem(DEFAULT_DATE_EVENEMENT.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent.nom(UPDATED_NOM).dateEvenement(UPDATED_DATE_EVENEMENT).prix(UPDATED_PRIX);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEvent.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
        assertThat(testEvent.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent.dateEvenement(UPDATED_DATE_EVENEMENT);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEvent.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
        assertThat(testEvent.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent.nom(UPDATED_NOM).dateEvenement(UPDATED_DATE_EVENEMENT).prix(UPDATED_PRIX);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEvent.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
        assertThat(testEvent.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
