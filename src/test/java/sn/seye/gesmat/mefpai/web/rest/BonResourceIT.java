package sn.seye.gesmat.mefpai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.seye.gesmat.mefpai.domain.Bon;
import sn.seye.gesmat.mefpai.repository.BonRepository;

/**
 * Integration tests for the {@link BonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BonResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonRepository bonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonMockMvc;

    private Bon bon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bon createEntity(EntityManager em) {
        Bon bon = new Bon().quantite(DEFAULT_QUANTITE).date(DEFAULT_DATE).description(DEFAULT_DESCRIPTION);
        return bon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bon createUpdatedEntity(EntityManager em) {
        Bon bon = new Bon().quantite(UPDATED_QUANTITE).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        return bon;
    }

    @BeforeEach
    public void initTest() {
        bon = createEntity(em);
    }

    @Test
    @Transactional
    void createBon() throws Exception {
        int databaseSizeBeforeCreate = bonRepository.findAll().size();
        // Create the Bon
        restBonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon)))
            .andExpect(status().isCreated());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate + 1);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testBon.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBonWithExistingId() throws Exception {
        // Create the Bon with an existing ID
        bon.setId(1L);

        int databaseSizeBeforeCreate = bonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon)))
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBons() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        // Get all the bonList
        restBonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bon.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        // Get the bon
        restBonMockMvc
            .perform(get(ENTITY_API_URL_ID, bon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bon.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBon() throws Exception {
        // Get the bon
        restBonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon
        Bon updatedBon = bonRepository.findById(bon.getId()).get();
        // Disconnect from session so that the updates on updatedBon are not directly saved in db
        em.detach(updatedBon);
        updatedBon.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testBon.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bon.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon.quantite(UPDATED_QUANTITE).description(UPDATED_DESCRIPTION);

        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testBon.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBonWithPatch() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeUpdate = bonRepository.findAll().size();

        // Update the bon using partial update
        Bon partialUpdatedBon = new Bon();
        partialUpdatedBon.setId(bon.getId());

        partialUpdatedBon.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBon))
            )
            .andExpect(status().isOk());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
        Bon testBon = bonList.get(bonList.size() - 1);
        assertThat(testBon.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testBon.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBon() throws Exception {
        int databaseSizeBeforeUpdate = bonRepository.findAll().size();
        bon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bon in the database
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBon() throws Exception {
        // Initialize the database
        bonRepository.saveAndFlush(bon);

        int databaseSizeBeforeDelete = bonRepository.findAll().size();

        // Delete the bon
        restBonMockMvc.perform(delete(ENTITY_API_URL_ID, bon.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bon> bonList = bonRepository.findAll();
        assertThat(bonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
