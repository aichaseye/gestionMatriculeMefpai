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
import sn.seye.gesmat.mefpai.domain.SerieEtude;
import sn.seye.gesmat.mefpai.domain.enumeration.Serie;
import sn.seye.gesmat.mefpai.repository.SerieEtudeRepository;

/**
 * Integration tests for the {@link SerieEtudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SerieEtudeResourceIT {

    private static final Serie DEFAULT_SERIE = Serie.STEG;
    private static final Serie UPDATED_SERIE = Serie.STIDD_E;

    private static final String ENTITY_API_URL = "/api/serie-etudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SerieEtudeRepository serieEtudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSerieEtudeMockMvc;

    private SerieEtude serieEtude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SerieEtude createEntity(EntityManager em) {
        SerieEtude serieEtude = new SerieEtude().serie(DEFAULT_SERIE);
        return serieEtude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SerieEtude createUpdatedEntity(EntityManager em) {
        SerieEtude serieEtude = new SerieEtude().serie(UPDATED_SERIE);
        return serieEtude;
    }

    @BeforeEach
    public void initTest() {
        serieEtude = createEntity(em);
    }

    @Test
    @Transactional
    void createSerieEtude() throws Exception {
        int databaseSizeBeforeCreate = serieEtudeRepository.findAll().size();
        // Create the SerieEtude
        restSerieEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serieEtude)))
            .andExpect(status().isCreated());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeCreate + 1);
        SerieEtude testSerieEtude = serieEtudeList.get(serieEtudeList.size() - 1);
        assertThat(testSerieEtude.getSerie()).isEqualTo(DEFAULT_SERIE);
    }

    @Test
    @Transactional
    void createSerieEtudeWithExistingId() throws Exception {
        // Create the SerieEtude with an existing ID
        serieEtude.setId(1L);

        int databaseSizeBeforeCreate = serieEtudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSerieEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serieEtude)))
            .andExpect(status().isBadRequest());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        int databaseSizeBeforeTest = serieEtudeRepository.findAll().size();
        // set the field null
        serieEtude.setSerie(null);

        // Create the SerieEtude, which fails.

        restSerieEtudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serieEtude)))
            .andExpect(status().isBadRequest());

        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSerieEtudes() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        // Get all the serieEtudeList
        restSerieEtudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serieEtude.getId().intValue())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())));
    }

    @Test
    @Transactional
    void getSerieEtude() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        // Get the serieEtude
        restSerieEtudeMockMvc
            .perform(get(ENTITY_API_URL_ID, serieEtude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serieEtude.getId().intValue()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSerieEtude() throws Exception {
        // Get the serieEtude
        restSerieEtudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSerieEtude() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();

        // Update the serieEtude
        SerieEtude updatedSerieEtude = serieEtudeRepository.findById(serieEtude.getId()).get();
        // Disconnect from session so that the updates on updatedSerieEtude are not directly saved in db
        em.detach(updatedSerieEtude);
        updatedSerieEtude.serie(UPDATED_SERIE);

        restSerieEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSerieEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSerieEtude))
            )
            .andExpect(status().isOk());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
        SerieEtude testSerieEtude = serieEtudeList.get(serieEtudeList.size() - 1);
        assertThat(testSerieEtude.getSerie()).isEqualTo(UPDATED_SERIE);
    }

    @Test
    @Transactional
    void putNonExistingSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serieEtude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serieEtude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSerieEtudeWithPatch() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();

        // Update the serieEtude using partial update
        SerieEtude partialUpdatedSerieEtude = new SerieEtude();
        partialUpdatedSerieEtude.setId(serieEtude.getId());

        partialUpdatedSerieEtude.serie(UPDATED_SERIE);

        restSerieEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSerieEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSerieEtude))
            )
            .andExpect(status().isOk());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
        SerieEtude testSerieEtude = serieEtudeList.get(serieEtudeList.size() - 1);
        assertThat(testSerieEtude.getSerie()).isEqualTo(UPDATED_SERIE);
    }

    @Test
    @Transactional
    void fullUpdateSerieEtudeWithPatch() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();

        // Update the serieEtude using partial update
        SerieEtude partialUpdatedSerieEtude = new SerieEtude();
        partialUpdatedSerieEtude.setId(serieEtude.getId());

        partialUpdatedSerieEtude.serie(UPDATED_SERIE);

        restSerieEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSerieEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSerieEtude))
            )
            .andExpect(status().isOk());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
        SerieEtude testSerieEtude = serieEtudeList.get(serieEtudeList.size() - 1);
        assertThat(testSerieEtude.getSerie()).isEqualTo(UPDATED_SERIE);
    }

    @Test
    @Transactional
    void patchNonExistingSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serieEtude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serieEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serieEtude))
            )
            .andExpect(status().isBadRequest());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSerieEtude() throws Exception {
        int databaseSizeBeforeUpdate = serieEtudeRepository.findAll().size();
        serieEtude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieEtudeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(serieEtude))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SerieEtude in the database
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSerieEtude() throws Exception {
        // Initialize the database
        serieEtudeRepository.saveAndFlush(serieEtude);

        int databaseSizeBeforeDelete = serieEtudeRepository.findAll().size();

        // Delete the serieEtude
        restSerieEtudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, serieEtude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SerieEtude> serieEtudeList = serieEtudeRepository.findAll();
        assertThat(serieEtudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
