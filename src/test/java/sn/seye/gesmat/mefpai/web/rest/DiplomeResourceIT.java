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
import sn.seye.gesmat.mefpai.domain.Diplome;
import sn.seye.gesmat.mefpai.domain.enumeration.NomDiplome;
import sn.seye.gesmat.mefpai.repository.DiplomeRepository;

/**
 * Integration tests for the {@link DiplomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiplomeResourceIT {

    private static final NomDiplome DEFAULT_NOM = NomDiplome.CAP;
    private static final NomDiplome UPDATED_NOM = NomDiplome.BTS;

    private static final String DEFAULT_MATRICULE_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_DIPLOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANNEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/diplomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DiplomeRepository diplomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiplomeMockMvc;

    private Diplome diplome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createEntity(EntityManager em) {
        Diplome diplome = new Diplome().nom(DEFAULT_NOM).matriculeDiplome(DEFAULT_MATRICULE_DIPLOME).annee(DEFAULT_ANNEE);
        return diplome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createUpdatedEntity(EntityManager em) {
        Diplome diplome = new Diplome().nom(UPDATED_NOM).matriculeDiplome(UPDATED_MATRICULE_DIPLOME).annee(UPDATED_ANNEE);
        return diplome;
    }

    @BeforeEach
    public void initTest() {
        diplome = createEntity(em);
    }

    @Test
    @Transactional
    void createDiplome() throws Exception {
        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();
        // Create the Diplome
        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isCreated());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate + 1);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDiplome.getMatriculeDiplome()).isEqualTo(DEFAULT_MATRICULE_DIPLOME);
        assertThat(testDiplome.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void createDiplomeWithExistingId() throws Exception {
        // Create the Diplome with an existing ID
        diplome.setId(1L);

        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setNom(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isBadRequest());

        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculeDiplomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setMatriculeDiplome(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isBadRequest());

        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiplomes() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get all the diplomeList
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diplome.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].matriculeDiplome").value(hasItem(DEFAULT_MATRICULE_DIPLOME)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())));
    }

    @Test
    @Transactional
    void getDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get the diplome
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL_ID, diplome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diplome.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.matriculeDiplome").value(DEFAULT_MATRICULE_DIPLOME))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDiplome() throws Exception {
        // Get the diplome
        restDiplomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome
        Diplome updatedDiplome = diplomeRepository.findById(diplome.getId()).get();
        // Disconnect from session so that the updates on updatedDiplome are not directly saved in db
        em.detach(updatedDiplome);
        updatedDiplome.nom(UPDATED_NOM).matriculeDiplome(UPDATED_MATRICULE_DIPLOME).annee(UPDATED_ANNEE);

        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDiplome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDiplome.getMatriculeDiplome()).isEqualTo(UPDATED_MATRICULE_DIPLOME);
        assertThat(testDiplome.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void putNonExistingDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diplome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome.annee(UPDATED_ANNEE);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDiplome.getMatriculeDiplome()).isEqualTo(DEFAULT_MATRICULE_DIPLOME);
        assertThat(testDiplome.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome.nom(UPDATED_NOM).matriculeDiplome(UPDATED_MATRICULE_DIPLOME).annee(UPDATED_ANNEE);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDiplome.getMatriculeDiplome()).isEqualTo(UPDATED_MATRICULE_DIPLOME);
        assertThat(testDiplome.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeDelete = diplomeRepository.findAll().size();

        // Delete the diplome
        restDiplomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, diplome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
