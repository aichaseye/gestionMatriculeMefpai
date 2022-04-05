package sn.seye.gesmat.mefpai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.seye.gesmat.mefpai.IntegrationTest;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;
import sn.seye.gesmat.mefpai.repository.NoteProgrammeRepository;
import sn.seye.gesmat.mefpai.service.NoteProgrammeService;

/**
 * Integration tests for the {@link NoteProgrammeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NoteProgrammeResourceIT {

    private static final String DEFAULT_NOM_PROG = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROG = "BBBBBBBBBB";

    private static final Integer DEFAULT_COEF = 1;
    private static final Integer UPDATED_COEF = 2;

    private static final Float DEFAULT_NOTE = 1F;
    private static final Float UPDATED_NOTE = 2F;

    private static final String ENTITY_API_URL = "/api/note-programmes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoteProgrammeRepository noteProgrammeRepository;

    @Mock
    private NoteProgrammeRepository noteProgrammeRepositoryMock;

    @Mock
    private NoteProgrammeService noteProgrammeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteProgrammeMockMvc;

    private NoteProgramme noteProgramme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteProgramme createEntity(EntityManager em) {
        NoteProgramme noteProgramme = new NoteProgramme().nomProg(DEFAULT_NOM_PROG).coef(DEFAULT_COEF).note(DEFAULT_NOTE);
        return noteProgramme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteProgramme createUpdatedEntity(EntityManager em) {
        NoteProgramme noteProgramme = new NoteProgramme().nomProg(UPDATED_NOM_PROG).coef(UPDATED_COEF).note(UPDATED_NOTE);
        return noteProgramme;
    }

    @BeforeEach
    public void initTest() {
        noteProgramme = createEntity(em);
    }

    @Test
    @Transactional
    void createNoteProgramme() throws Exception {
        int databaseSizeBeforeCreate = noteProgrammeRepository.findAll().size();
        // Create the NoteProgramme
        restNoteProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(noteProgramme)))
            .andExpect(status().isCreated());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeCreate + 1);
        NoteProgramme testNoteProgramme = noteProgrammeList.get(noteProgrammeList.size() - 1);
        assertThat(testNoteProgramme.getNomProg()).isEqualTo(DEFAULT_NOM_PROG);
        assertThat(testNoteProgramme.getCoef()).isEqualTo(DEFAULT_COEF);
        assertThat(testNoteProgramme.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createNoteProgrammeWithExistingId() throws Exception {
        // Create the NoteProgramme with an existing ID
        noteProgramme.setId(1L);

        int databaseSizeBeforeCreate = noteProgrammeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(noteProgramme)))
            .andExpect(status().isBadRequest());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNoteProgrammes() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        // Get all the noteProgrammeList
        restNoteProgrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noteProgramme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProg").value(hasItem(DEFAULT_NOM_PROG)))
            .andExpect(jsonPath("$.[*].coef").value(hasItem(DEFAULT_COEF)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNoteProgrammesWithEagerRelationshipsIsEnabled() throws Exception {
        when(noteProgrammeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNoteProgrammeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(noteProgrammeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNoteProgrammesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(noteProgrammeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNoteProgrammeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(noteProgrammeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getNoteProgramme() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        // Get the noteProgramme
        restNoteProgrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, noteProgramme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(noteProgramme.getId().intValue()))
            .andExpect(jsonPath("$.nomProg").value(DEFAULT_NOM_PROG))
            .andExpect(jsonPath("$.coef").value(DEFAULT_COEF))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingNoteProgramme() throws Exception {
        // Get the noteProgramme
        restNoteProgrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNoteProgramme() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();

        // Update the noteProgramme
        NoteProgramme updatedNoteProgramme = noteProgrammeRepository.findById(noteProgramme.getId()).get();
        // Disconnect from session so that the updates on updatedNoteProgramme are not directly saved in db
        em.detach(updatedNoteProgramme);
        updatedNoteProgramme.nomProg(UPDATED_NOM_PROG).coef(UPDATED_COEF).note(UPDATED_NOTE);

        restNoteProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNoteProgramme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNoteProgramme))
            )
            .andExpect(status().isOk());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
        NoteProgramme testNoteProgramme = noteProgrammeList.get(noteProgrammeList.size() - 1);
        assertThat(testNoteProgramme.getNomProg()).isEqualTo(UPDATED_NOM_PROG);
        assertThat(testNoteProgramme.getCoef()).isEqualTo(UPDATED_COEF);
        assertThat(testNoteProgramme.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, noteProgramme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteProgramme))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noteProgramme))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(noteProgramme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNoteProgrammeWithPatch() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();

        // Update the noteProgramme using partial update
        NoteProgramme partialUpdatedNoteProgramme = new NoteProgramme();
        partialUpdatedNoteProgramme.setId(noteProgramme.getId());

        restNoteProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNoteProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNoteProgramme))
            )
            .andExpect(status().isOk());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
        NoteProgramme testNoteProgramme = noteProgrammeList.get(noteProgrammeList.size() - 1);
        assertThat(testNoteProgramme.getNomProg()).isEqualTo(DEFAULT_NOM_PROG);
        assertThat(testNoteProgramme.getCoef()).isEqualTo(DEFAULT_COEF);
        assertThat(testNoteProgramme.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateNoteProgrammeWithPatch() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();

        // Update the noteProgramme using partial update
        NoteProgramme partialUpdatedNoteProgramme = new NoteProgramme();
        partialUpdatedNoteProgramme.setId(noteProgramme.getId());

        partialUpdatedNoteProgramme.nomProg(UPDATED_NOM_PROG).coef(UPDATED_COEF).note(UPDATED_NOTE);

        restNoteProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNoteProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNoteProgramme))
            )
            .andExpect(status().isOk());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
        NoteProgramme testNoteProgramme = noteProgrammeList.get(noteProgrammeList.size() - 1);
        assertThat(testNoteProgramme.getNomProg()).isEqualTo(UPDATED_NOM_PROG);
        assertThat(testNoteProgramme.getCoef()).isEqualTo(UPDATED_COEF);
        assertThat(testNoteProgramme.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, noteProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noteProgramme))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noteProgramme))
            )
            .andExpect(status().isBadRequest());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNoteProgramme() throws Exception {
        int databaseSizeBeforeUpdate = noteProgrammeRepository.findAll().size();
        noteProgramme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(noteProgramme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NoteProgramme in the database
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNoteProgramme() throws Exception {
        // Initialize the database
        noteProgrammeRepository.saveAndFlush(noteProgramme);

        int databaseSizeBeforeDelete = noteProgrammeRepository.findAll().size();

        // Delete the noteProgramme
        restNoteProgrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, noteProgramme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NoteProgramme> noteProgrammeList = noteProgrammeRepository.findAll();
        assertThat(noteProgrammeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
