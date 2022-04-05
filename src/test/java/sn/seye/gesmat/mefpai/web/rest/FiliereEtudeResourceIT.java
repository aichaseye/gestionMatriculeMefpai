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
import sn.seye.gesmat.mefpai.domain.FiliereEtude;
import sn.seye.gesmat.mefpai.domain.enumeration.Filiere;
import sn.seye.gesmat.mefpai.repository.FiliereEtudeRepository;

/**
 * Integration tests for the {@link FiliereEtudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiliereEtudeResourceIT {

    private static final Filiere DEFAULT_FILIERE = Filiere.Agricultre;
    private static final Filiere UPDATED_FILIERE = Filiere.Peche;

    private static final String ENTITY_API_URL = "/api/filiere-etudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiliereEtudeRepository filiereEtudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiliereEtudeMockMvc;

    private FiliereEtude filiereEtude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereEtude createEntity(EntityManager em) {
        FiliereEtude filiereEtude = new FiliereEtude().filiere(DEFAULT_FILIERE);
        return filiereEtude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiliereEtude createUpdatedEntity(EntityManager em) {
        FiliereEtude filiereEtude = new FiliereEtude().filiere(UPDATED_FILIERE);
        return filiereEtude;
    }

    @BeforeEach
    public void initTest() {
        filiereEtude = createEntity(em);
    }

    @Test
    @Transactional
    void createFiliereEtude() throws Exception {
        int databaseSizeBeforeCreate = filiereEtudeRepository.findAll().size();
        // Create the FiliereEtude
        restFiliereEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filiereEtude)))
            .andExpect(status().isCreated());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeCreate + 1);
        FiliereEtude testFiliereEtude = filiereEtudeList.get(filiereEtudeList.size() - 1);
        assertThat(testFiliereEtude.getFiliere()).isEqualTo(DEFAULT_FILIERE);
    }

    @Test
    @Transactional
    void createFiliereEtudeWithExistingId() throws Exception {
        // Create the FiliereEtude with an existing ID
        filiereEtude.setId(1L);

        int databaseSizeBeforeCreate = filiereEtudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiliereEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filiereEtude)))
            .andExpect(status().isBadRequest());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFiliereIsRequired() throws Exception {
        int databaseSizeBeforeTest = filiereEtudeRepository.findAll().size();
        // set the field null
        filiereEtude.setFiliere(null);

        // Create the FiliereEtude, which fails.

        restFiliereEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filiereEtude)))
            .andExpect(status().isBadRequest());

        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiliereEtudes() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        // Get all the filiereEtudeList
        restFiliereEtudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiereEtude.getId().intValue())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())));
    }

    @Test
    @Transactional
    void getFiliereEtude() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        // Get the filiereEtude
        restFiliereEtudeMockMvc
            .perform(get(ENTITY_API_URL_ID, filiereEtude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiereEtude.getId().intValue()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFiliereEtude() throws Exception {
        // Get the filiereEtude
        restFiliereEtudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiliereEtude() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();

        // Update the filiereEtude
        FiliereEtude updatedFiliereEtude = filiereEtudeRepository.findById(filiereEtude.getId()).get();
        // Disconnect from session so that the updates on updatedFiliereEtude are not directly saved in db
        em.detach(updatedFiliereEtude);
        updatedFiliereEtude.filiere(UPDATED_FILIERE);

        restFiliereEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFiliereEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFiliereEtude))
            )
            .andExpect(status().isOk());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
        FiliereEtude testFiliereEtude = filiereEtudeList.get(filiereEtudeList.size() - 1);
        assertThat(testFiliereEtude.getFiliere()).isEqualTo(UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void putNonExistingFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filiereEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filiereEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filiereEtude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiliereEtudeWithPatch() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();

        // Update the filiereEtude using partial update
        FiliereEtude partialUpdatedFiliereEtude = new FiliereEtude();
        partialUpdatedFiliereEtude.setId(filiereEtude.getId());

        partialUpdatedFiliereEtude.filiere(UPDATED_FILIERE);

        restFiliereEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliereEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereEtude))
            )
            .andExpect(status().isOk());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
        FiliereEtude testFiliereEtude = filiereEtudeList.get(filiereEtudeList.size() - 1);
        assertThat(testFiliereEtude.getFiliere()).isEqualTo(UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void fullUpdateFiliereEtudeWithPatch() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();

        // Update the filiereEtude using partial update
        FiliereEtude partialUpdatedFiliereEtude = new FiliereEtude();
        partialUpdatedFiliereEtude.setId(filiereEtude.getId());

        partialUpdatedFiliereEtude.filiere(UPDATED_FILIERE);

        restFiliereEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliereEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliereEtude))
            )
            .andExpect(status().isOk());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
        FiliereEtude testFiliereEtude = filiereEtudeList.get(filiereEtudeList.size() - 1);
        assertThat(testFiliereEtude.getFiliere()).isEqualTo(UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void patchNonExistingFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filiereEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filiereEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filiereEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiliereEtude() throws Exception {
        int databaseSizeBeforeUpdate = filiereEtudeRepository.findAll().size();
        filiereEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filiereEtude))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiliereEtude in the database
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiliereEtude() throws Exception {
        // Initialize the database
        filiereEtudeRepository.saveAndFlush(filiereEtude);

        int databaseSizeBeforeDelete = filiereEtudeRepository.findAll().size();

        // Delete the filiereEtude
        restFiliereEtudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, filiereEtude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiliereEtude> filiereEtudeList = filiereEtudeRepository.findAll();
        assertThat(filiereEtudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
