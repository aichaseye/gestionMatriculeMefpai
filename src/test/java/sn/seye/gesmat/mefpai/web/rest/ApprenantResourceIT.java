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
import org.springframework.util.Base64Utils;
import sn.seye.gesmat.mefpai.IntegrationTest;
import sn.seye.gesmat.mefpai.domain.Apprenant;
import sn.seye.gesmat.mefpai.domain.enumeration.Sexe;
import sn.seye.gesmat.mefpai.domain.enumeration.StatutApp;
import sn.seye.gesmat.mefpai.repository.ApprenantRepository;

/**
 * Integration tests for the {@link ApprenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprenantResourceIT {

    private static final String DEFAULT_NOM_COMPLET_APP = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET_APP = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_ACTE_NAISSANCE = 1;
    private static final Integer UPDATED_NUM_ACTE_NAISSANCE = 2;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Sexe DEFAULT_SEXE = Sexe.Masclin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final StatutApp DEFAULT_STATUT = StatutApp.Prive;
    private static final StatutApp UPDATED_STATUT = StatutApp.Publique;

    private static final String DEFAULT_SITUATION_MATRIMONIALE = "AAAAAAAAAA";
    private static final String UPDATED_SITUATION_MATRIMONIALE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_APP = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_APP = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apprenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprenantMockMvc;

    private Apprenant apprenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apprenant createEntity(EntityManager em) {
        Apprenant apprenant = new Apprenant()
            .nomCompletApp(DEFAULT_NOM_COMPLET_APP)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .numActeNaissance(DEFAULT_NUM_ACTE_NAISSANCE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .sexe(DEFAULT_SEXE)
            .adresse(DEFAULT_ADRESSE)
            .statut(DEFAULT_STATUT)
            .situationMatrimoniale(DEFAULT_SITUATION_MATRIMONIALE)
            .matriculeApp(DEFAULT_MATRICULE_APP)
            .nationalite(DEFAULT_NATIONALITE);
        return apprenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apprenant createUpdatedEntity(EntityManager em) {
        Apprenant apprenant = new Apprenant()
            .nomCompletApp(UPDATED_NOM_COMPLET_APP)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numActeNaissance(UPDATED_NUM_ACTE_NAISSANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .sexe(UPDATED_SEXE)
            .adresse(UPDATED_ADRESSE)
            .statut(UPDATED_STATUT)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .matriculeApp(UPDATED_MATRICULE_APP)
            .nationalite(UPDATED_NATIONALITE);
        return apprenant;
    }

    @BeforeEach
    public void initTest() {
        apprenant = createEntity(em);
    }

    @Test
    @Transactional
    void createApprenant() throws Exception {
        int databaseSizeBeforeCreate = apprenantRepository.findAll().size();
        // Create the Apprenant
        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isCreated());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeCreate + 1);
        Apprenant testApprenant = apprenantList.get(apprenantList.size() - 1);
        assertThat(testApprenant.getNomCompletApp()).isEqualTo(DEFAULT_NOM_COMPLET_APP);
        assertThat(testApprenant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApprenant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testApprenant.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testApprenant.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testApprenant.getNumActeNaissance()).isEqualTo(DEFAULT_NUM_ACTE_NAISSANCE);
        assertThat(testApprenant.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testApprenant.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testApprenant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testApprenant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testApprenant.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testApprenant.getSituationMatrimoniale()).isEqualTo(DEFAULT_SITUATION_MATRIMONIALE);
        assertThat(testApprenant.getMatriculeApp()).isEqualTo(DEFAULT_MATRICULE_APP);
        assertThat(testApprenant.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
    }

    @Test
    @Transactional
    void createApprenantWithExistingId() throws Exception {
        // Create the Apprenant with an existing ID
        apprenant.setId(1L);

        int databaseSizeBeforeCreate = apprenantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCompletAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setNomCompletApp(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setEmail(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setTelephone(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setSexe(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setAdresse(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNationaliteIsRequired() throws Exception {
        int databaseSizeBeforeTest = apprenantRepository.findAll().size();
        // set the field null
        apprenant.setNationalite(null);

        // Create the Apprenant, which fails.

        restApprenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isBadRequest());

        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprenants() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        // Get all the apprenantList
        restApprenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apprenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCompletApp").value(hasItem(DEFAULT_NOM_COMPLET_APP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].numActeNaissance").value(hasItem(DEFAULT_NUM_ACTE_NAISSANCE)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].situationMatrimoniale").value(hasItem(DEFAULT_SITUATION_MATRIMONIALE)))
            .andExpect(jsonPath("$.[*].matriculeApp").value(hasItem(DEFAULT_MATRICULE_APP)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)));
    }

    @Test
    @Transactional
    void getApprenant() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        // Get the apprenant
        restApprenantMockMvc
            .perform(get(ENTITY_API_URL_ID, apprenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apprenant.getId().intValue()))
            .andExpect(jsonPath("$.nomCompletApp").value(DEFAULT_NOM_COMPLET_APP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.numActeNaissance").value(DEFAULT_NUM_ACTE_NAISSANCE))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.situationMatrimoniale").value(DEFAULT_SITUATION_MATRIMONIALE))
            .andExpect(jsonPath("$.matriculeApp").value(DEFAULT_MATRICULE_APP))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE));
    }

    @Test
    @Transactional
    void getNonExistingApprenant() throws Exception {
        // Get the apprenant
        restApprenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprenant() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();

        // Update the apprenant
        Apprenant updatedApprenant = apprenantRepository.findById(apprenant.getId()).get();
        // Disconnect from session so that the updates on updatedApprenant are not directly saved in db
        em.detach(updatedApprenant);
        updatedApprenant
            .nomCompletApp(UPDATED_NOM_COMPLET_APP)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numActeNaissance(UPDATED_NUM_ACTE_NAISSANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .sexe(UPDATED_SEXE)
            .adresse(UPDATED_ADRESSE)
            .statut(UPDATED_STATUT)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .matriculeApp(UPDATED_MATRICULE_APP)
            .nationalite(UPDATED_NATIONALITE);

        restApprenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprenant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprenant))
            )
            .andExpect(status().isOk());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
        Apprenant testApprenant = apprenantList.get(apprenantList.size() - 1);
        assertThat(testApprenant.getNomCompletApp()).isEqualTo(UPDATED_NOM_COMPLET_APP);
        assertThat(testApprenant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprenant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testApprenant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testApprenant.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testApprenant.getNumActeNaissance()).isEqualTo(UPDATED_NUM_ACTE_NAISSANCE);
        assertThat(testApprenant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testApprenant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testApprenant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testApprenant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testApprenant.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testApprenant.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testApprenant.getMatriculeApp()).isEqualTo(UPDATED_MATRICULE_APP);
        assertThat(testApprenant.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void putNonExistingApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apprenant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apprenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apprenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apprenant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprenantWithPatch() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();

        // Update the apprenant using partial update
        Apprenant partialUpdatedApprenant = new Apprenant();
        partialUpdatedApprenant.setId(apprenant.getId());

        partialUpdatedApprenant
            .email(UPDATED_EMAIL)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numActeNaissance(UPDATED_NUM_ACTE_NAISSANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .sexe(UPDATED_SEXE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .matriculeApp(UPDATED_MATRICULE_APP);

        restApprenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprenant))
            )
            .andExpect(status().isOk());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
        Apprenant testApprenant = apprenantList.get(apprenantList.size() - 1);
        assertThat(testApprenant.getNomCompletApp()).isEqualTo(DEFAULT_NOM_COMPLET_APP);
        assertThat(testApprenant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprenant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testApprenant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testApprenant.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testApprenant.getNumActeNaissance()).isEqualTo(UPDATED_NUM_ACTE_NAISSANCE);
        assertThat(testApprenant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testApprenant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testApprenant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testApprenant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testApprenant.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testApprenant.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testApprenant.getMatriculeApp()).isEqualTo(UPDATED_MATRICULE_APP);
        assertThat(testApprenant.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
    }

    @Test
    @Transactional
    void fullUpdateApprenantWithPatch() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();

        // Update the apprenant using partial update
        Apprenant partialUpdatedApprenant = new Apprenant();
        partialUpdatedApprenant.setId(apprenant.getId());

        partialUpdatedApprenant
            .nomCompletApp(UPDATED_NOM_COMPLET_APP)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numActeNaissance(UPDATED_NUM_ACTE_NAISSANCE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .sexe(UPDATED_SEXE)
            .adresse(UPDATED_ADRESSE)
            .statut(UPDATED_STATUT)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .matriculeApp(UPDATED_MATRICULE_APP)
            .nationalite(UPDATED_NATIONALITE);

        restApprenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprenant))
            )
            .andExpect(status().isOk());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
        Apprenant testApprenant = apprenantList.get(apprenantList.size() - 1);
        assertThat(testApprenant.getNomCompletApp()).isEqualTo(UPDATED_NOM_COMPLET_APP);
        assertThat(testApprenant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprenant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testApprenant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testApprenant.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testApprenant.getNumActeNaissance()).isEqualTo(UPDATED_NUM_ACTE_NAISSANCE);
        assertThat(testApprenant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testApprenant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testApprenant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testApprenant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testApprenant.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testApprenant.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testApprenant.getMatriculeApp()).isEqualTo(UPDATED_MATRICULE_APP);
        assertThat(testApprenant.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void patchNonExistingApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apprenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apprenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apprenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprenant() throws Exception {
        int databaseSizeBeforeUpdate = apprenantRepository.findAll().size();
        apprenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprenantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apprenant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apprenant in the database
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprenant() throws Exception {
        // Initialize the database
        apprenantRepository.saveAndFlush(apprenant);

        int databaseSizeBeforeDelete = apprenantRepository.findAll().size();

        // Delete the apprenant
        restApprenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, apprenant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apprenant> apprenantList = apprenantRepository.findAll();
        assertThat(apprenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
