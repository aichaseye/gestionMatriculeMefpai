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
import sn.seye.gesmat.mefpai.domain.DemandeMatEtab;
import sn.seye.gesmat.mefpai.repository.DemandeMatEtabRepository;

/**
 * Integration tests for the {@link DemandeMatEtabResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeMatEtabResourceIT {

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/demande-mat-etabs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeMatEtabRepository demandeMatEtabRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMatEtabMockMvc;

    private DemandeMatEtab demandeMatEtab;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeMatEtab createEntity(EntityManager em) {
        DemandeMatEtab demandeMatEtab = new DemandeMatEtab()
            .objet(DEFAULT_OBJET)
            .description(DEFAULT_DESCRIPTION)
            .dateDemande(DEFAULT_DATE_DEMANDE);
        return demandeMatEtab;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeMatEtab createUpdatedEntity(EntityManager em) {
        DemandeMatEtab demandeMatEtab = new DemandeMatEtab()
            .objet(UPDATED_OBJET)
            .description(UPDATED_DESCRIPTION)
            .dateDemande(UPDATED_DATE_DEMANDE);
        return demandeMatEtab;
    }

    @BeforeEach
    public void initTest() {
        demandeMatEtab = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeMatEtab() throws Exception {
        int databaseSizeBeforeCreate = demandeMatEtabRepository.findAll().size();
        // Create the DemandeMatEtab
        restDemandeMatEtabMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeMatEtab testDemandeMatEtab = demandeMatEtabList.get(demandeMatEtabList.size() - 1);
        assertThat(testDemandeMatEtab.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testDemandeMatEtab.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDemandeMatEtab.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
    }

    @Test
    @Transactional
    void createDemandeMatEtabWithExistingId() throws Exception {
        // Create the DemandeMatEtab with an existing ID
        demandeMatEtab.setId(1L);

        int databaseSizeBeforeCreate = demandeMatEtabRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMatEtabMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandeMatEtabs() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        // Get all the demandeMatEtabList
        restDemandeMatEtabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeMatEtab.getId().intValue())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())));
    }

    @Test
    @Transactional
    void getDemandeMatEtab() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        // Get the demandeMatEtab
        restDemandeMatEtabMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeMatEtab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeMatEtab.getId().intValue()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDemandeMatEtab() throws Exception {
        // Get the demandeMatEtab
        restDemandeMatEtabMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeMatEtab() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();

        // Update the demandeMatEtab
        DemandeMatEtab updatedDemandeMatEtab = demandeMatEtabRepository.findById(demandeMatEtab.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeMatEtab are not directly saved in db
        em.detach(updatedDemandeMatEtab);
        updatedDemandeMatEtab.objet(UPDATED_OBJET).description(UPDATED_DESCRIPTION).dateDemande(UPDATED_DATE_DEMANDE);

        restDemandeMatEtabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeMatEtab.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeMatEtab))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatEtab testDemandeMatEtab = demandeMatEtabList.get(demandeMatEtabList.size() - 1);
        assertThat(testDemandeMatEtab.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testDemandeMatEtab.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDemandeMatEtab.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
    }

    @Test
    @Transactional
    void putNonExistingDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeMatEtab.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatEtab)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeMatEtabWithPatch() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();

        // Update the demandeMatEtab using partial update
        DemandeMatEtab partialUpdatedDemandeMatEtab = new DemandeMatEtab();
        partialUpdatedDemandeMatEtab.setId(demandeMatEtab.getId());

        restDemandeMatEtabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeMatEtab.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeMatEtab))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatEtab testDemandeMatEtab = demandeMatEtabList.get(demandeMatEtabList.size() - 1);
        assertThat(testDemandeMatEtab.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testDemandeMatEtab.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDemandeMatEtab.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
    }

    @Test
    @Transactional
    void fullUpdateDemandeMatEtabWithPatch() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();

        // Update the demandeMatEtab using partial update
        DemandeMatEtab partialUpdatedDemandeMatEtab = new DemandeMatEtab();
        partialUpdatedDemandeMatEtab.setId(demandeMatEtab.getId());

        partialUpdatedDemandeMatEtab.objet(UPDATED_OBJET).description(UPDATED_DESCRIPTION).dateDemande(UPDATED_DATE_DEMANDE);

        restDemandeMatEtabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeMatEtab.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeMatEtab))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatEtab testDemandeMatEtab = demandeMatEtabList.get(demandeMatEtabList.size() - 1);
        assertThat(testDemandeMatEtab.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testDemandeMatEtab.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDemandeMatEtab.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeMatEtab.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeMatEtab() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatEtabRepository.findAll().size();
        demandeMatEtab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatEtabMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(demandeMatEtab))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeMatEtab in the database
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeMatEtab() throws Exception {
        // Initialize the database
        demandeMatEtabRepository.saveAndFlush(demandeMatEtab);

        int databaseSizeBeforeDelete = demandeMatEtabRepository.findAll().size();

        // Delete the demandeMatEtab
        restDemandeMatEtabMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeMatEtab.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeMatEtab> demandeMatEtabList = demandeMatEtabRepository.findAll();
        assertThat(demandeMatEtabList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
