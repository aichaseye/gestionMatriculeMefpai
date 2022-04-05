package sn.seye.gesmat.mefpai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.seye.gesmat.mefpai.IntegrationTest;
import sn.seye.gesmat.mefpai.domain.NiveauEtude;
import sn.seye.gesmat.mefpai.domain.enumeration.Niveau;
import sn.seye.gesmat.mefpai.repository.NiveauEtudeRepository;

/**
 * Integration tests for the {@link NiveauEtudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NiveauEtudeResourceIT {

    private static final Niveau DEFAULT_NIVEAU = Niveau.CAP1;
    private static final Niveau UPDATED_NIVEAU = Niveau.CAP2;

    private static final String ENTITY_API_URL = "/api/niveau-etudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NiveauEtudeRepository niveauEtudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNiveauEtudeMockMvc;

    private NiveauEtude niveauEtude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NiveauEtude createEntity(EntityManager em) {
        NiveauEtude niveauEtude = new NiveauEtude().niveau(DEFAULT_NIVEAU);
        return niveauEtude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NiveauEtude createUpdatedEntity(EntityManager em) {
        NiveauEtude niveauEtude = new NiveauEtude().niveau(UPDATED_NIVEAU);
        return niveauEtude;
    }

    @BeforeEach
    public void initTest() {
        niveauEtude = createEntity(em);
    }

    @Test
    @Transactional
    void createNiveauEtude() throws Exception {
        int databaseSizeBeforeCreate = niveauEtudeRepository.findAll().size();
        // Create the NiveauEtude
        restNiveauEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(niveauEtude)))
            .andExpect(status().isCreated());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeCreate + 1);
        NiveauEtude testNiveauEtude = niveauEtudeList.get(niveauEtudeList.size() - 1);
        assertThat(testNiveauEtude.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
    }

    @Test
    @Transactional
    void createNiveauEtudeWithExistingId() throws Exception {
        // Create the NiveauEtude with an existing ID
        niveauEtude.setId(1L);

        int databaseSizeBeforeCreate = niveauEtudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNiveauEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(niveauEtude)))
            .andExpect(status().isBadRequest());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = niveauEtudeRepository.findAll().size();
        // set the field null
        niveauEtude.setNiveau(null);

        // Create the NiveauEtude, which fails.

        restNiveauEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(niveauEtude)))
            .andExpect(status().isBadRequest());

        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNiveauEtudes() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        // Get all the niveauEtudeList
        restNiveauEtudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveauEtude.getId().intValue())))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }

    @Test
    @Transactional
    void getNiveauEtude() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        // Get the niveauEtude
        restNiveauEtudeMockMvc
            .perform(get(ENTITY_API_URL_ID, niveauEtude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(niveauEtude.getId().intValue()))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNiveauEtude() throws Exception {
        // Get the niveauEtude
        restNiveauEtudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNiveauEtude() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();

        // Update the niveauEtude
        NiveauEtude updatedNiveauEtude = niveauEtudeRepository.findById(niveauEtude.getId()).get();
        // Disconnect from session so that the updates on updatedNiveauEtude are not directly saved in db
        em.detach(updatedNiveauEtude);
        updatedNiveauEtude.niveau(UPDATED_NIVEAU);

        restNiveauEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNiveauEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNiveauEtude))
            )
            .andExpect(status().isOk());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
        NiveauEtude testNiveauEtude = niveauEtudeList.get(niveauEtudeList.size() - 1);
        assertThat(testNiveauEtude.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void putNonExistingNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, niveauEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(niveauEtude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNiveauEtudeWithPatch() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();

        // Update the niveauEtude using partial update
        NiveauEtude partialUpdatedNiveauEtude = new NiveauEtude();
        partialUpdatedNiveauEtude.setId(niveauEtude.getId());

        partialUpdatedNiveauEtude.niveau(UPDATED_NIVEAU);

        restNiveauEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveauEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNiveauEtude))
            )
            .andExpect(status().isOk());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
        NiveauEtude testNiveauEtude = niveauEtudeList.get(niveauEtudeList.size() - 1);
        assertThat(testNiveauEtude.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void fullUpdateNiveauEtudeWithPatch() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();

        // Update the niveauEtude using partial update
        NiveauEtude partialUpdatedNiveauEtude = new NiveauEtude();
        partialUpdatedNiveauEtude.setId(niveauEtude.getId());

        partialUpdatedNiveauEtude.niveau(UPDATED_NIVEAU);

        restNiveauEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveauEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNiveauEtude))
            )
            .andExpect(status().isOk());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
        NiveauEtude testNiveauEtude = niveauEtudeList.get(niveauEtudeList.size() - 1);
        assertThat(testNiveauEtude.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void patchNonExistingNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, niveauEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(niveauEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(niveauEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNiveauEtude() throws Exception {
        int databaseSizeBeforeUpdate = niveauEtudeRepository.findAll().size();
        niveauEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(niveauEtude))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NiveauEtude in the database
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNiveauEtude() throws Exception {
        // Initialize the database
        niveauEtudeRepository.saveAndFlush(niveauEtude);

        int databaseSizeBeforeDelete = niveauEtudeRepository.findAll().size();

        // Delete the niveauEtude
        restNiveauEtudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, niveauEtude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NiveauEtude> niveauEtudeList = niveauEtudeRepository.findAll();
        assertThat(niveauEtudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
