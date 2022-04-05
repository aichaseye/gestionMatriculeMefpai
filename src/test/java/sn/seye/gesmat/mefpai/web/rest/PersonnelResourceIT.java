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
import sn.seye.gesmat.mefpai.domain.Personnel;
import sn.seye.gesmat.mefpai.repository.PersonnelRepository;

/**
 * Integration tests for the {@link PersonnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonnelResourceIT {

    private static final String DEFAULT_NOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERS = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PERS = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PERS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personnel";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnelMockMvc;

    private Personnel personnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createEntity(EntityManager em) {
        Personnel personnel = new Personnel().nomPers(DEFAULT_NOM_PERS).prenomPers(DEFAULT_PRENOM_PERS).email(DEFAULT_EMAIL);
        return personnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createUpdatedEntity(EntityManager em) {
        Personnel personnel = new Personnel().nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS).email(UPDATED_EMAIL);
        return personnel;
    }

    @BeforeEach
    public void initTest() {
        personnel = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonnel() throws Exception {
        int databaseSizeBeforeCreate = personnelRepository.findAll().size();
        // Create the Personnel
        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isCreated());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeCreate + 1);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testPersonnel.getPrenomPers()).isEqualTo(DEFAULT_PRENOM_PERS);
        assertThat(testPersonnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createPersonnelWithExistingId() throws Exception {
        // Create the Personnel with an existing ID
        personnel.setId(1L);

        int databaseSizeBeforeCreate = personnelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnelRepository.findAll().size();
        // set the field null
        personnel.setNomPers(null);

        // Create the Personnel, which fails.

        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isBadRequest());

        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomPersIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnelRepository.findAll().size();
        // set the field null
        personnel.setPrenomPers(null);

        // Create the Personnel, which fails.

        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isBadRequest());

        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnelRepository.findAll().size();
        // set the field null
        personnel.setEmail(null);

        // Create the Personnel, which fails.

        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isBadRequest());

        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get all the personnelList
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPers").value(hasItem(DEFAULT_NOM_PERS)))
            .andExpect(jsonPath("$.[*].prenomPers").value(hasItem(DEFAULT_PRENOM_PERS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get the personnel
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL_ID, personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnel.getId().intValue()))
            .andExpect(jsonPath("$.nomPers").value(DEFAULT_NOM_PERS))
            .andExpect(jsonPath("$.prenomPers").value(DEFAULT_PRENOM_PERS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingPersonnel() throws Exception {
        // Get the personnel
        restPersonnelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel
        Personnel updatedPersonnel = personnelRepository.findById(personnel.getId()).get();
        // Disconnect from session so that the updates on updatedPersonnel are not directly saved in db
        em.detach(updatedPersonnel);
        updatedPersonnel.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS).email(UPDATED_EMAIL);

        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonnel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testPersonnel.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel.prenomPers(UPDATED_PRENOM_PERS).email(UPDATED_EMAIL);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNomPers()).isEqualTo(DEFAULT_NOM_PERS);
        assertThat(testPersonnel.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel.nomPers(UPDATED_NOM_PERS).prenomPers(UPDATED_PRENOM_PERS).email(UPDATED_EMAIL);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNomPers()).isEqualTo(UPDATED_NOM_PERS);
        assertThat(testPersonnel.getPrenomPers()).isEqualTo(UPDATED_PRENOM_PERS);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personnel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeDelete = personnelRepository.findAll().size();

        // Delete the personnel
        restPersonnelMockMvc
            .perform(delete(ENTITY_API_URL_ID, personnel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
