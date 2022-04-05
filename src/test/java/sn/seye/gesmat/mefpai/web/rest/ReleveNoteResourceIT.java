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
import sn.seye.gesmat.mefpai.domain.ReleveNote;
import sn.seye.gesmat.mefpai.domain.enumeration.Etat;
import sn.seye.gesmat.mefpai.domain.enumeration.NomSemestre;
import sn.seye.gesmat.mefpai.repository.ReleveNoteRepository;

/**
 * Integration tests for the {@link ReleveNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReleveNoteResourceIT {

    private static final LocalDate DEFAULT_ANNEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE = LocalDate.now(ZoneId.systemDefault());

    private static final Etat DEFAULT_ETAT = Etat.REDOUBLANT;
    private static final Etat UPDATED_ETAT = Etat.PASSANT;

    private static final String DEFAULT_APRECIATION = "AAAAAAAAAA";
    private static final String UPDATED_APRECIATION = "BBBBBBBBBB";

    private static final Float DEFAULT_MOYENNE = 1F;
    private static final Float UPDATED_MOYENNE = 2F;

    private static final Float DEFAULT_MOYENNE_GENERALE = 1F;
    private static final Float UPDATED_MOYENNE_GENERALE = 2F;

    private static final String DEFAULT_RANG = "AAAAAAAAAA";
    private static final String UPDATED_RANG = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOTE_CONDUITE = 1;
    private static final Integer UPDATED_NOTE_CONDUITE = 2;

    private static final NomSemestre DEFAULT_NOM_SEMESTRE = NomSemestre.Semestre1;
    private static final NomSemestre UPDATED_NOM_SEMESTRE = NomSemestre.Semestre2;

    private static final String DEFAULT_MATRICULE_REL = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_REL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/releve-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReleveNoteRepository releveNoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleveNoteMockMvc;

    private ReleveNote releveNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveNote createEntity(EntityManager em) {
        ReleveNote releveNote = new ReleveNote()
            .annee(DEFAULT_ANNEE)
            .etat(DEFAULT_ETAT)
            .apreciation(DEFAULT_APRECIATION)
            .moyenne(DEFAULT_MOYENNE)
            .moyenneGenerale(DEFAULT_MOYENNE_GENERALE)
            .rang(DEFAULT_RANG)
            .noteConduite(DEFAULT_NOTE_CONDUITE)
            .nomSemestre(DEFAULT_NOM_SEMESTRE)
            .matriculeRel(DEFAULT_MATRICULE_REL);
        return releveNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveNote createUpdatedEntity(EntityManager em) {
        ReleveNote releveNote = new ReleveNote()
            .annee(UPDATED_ANNEE)
            .etat(UPDATED_ETAT)
            .apreciation(UPDATED_APRECIATION)
            .moyenne(UPDATED_MOYENNE)
            .moyenneGenerale(UPDATED_MOYENNE_GENERALE)
            .rang(UPDATED_RANG)
            .noteConduite(UPDATED_NOTE_CONDUITE)
            .nomSemestre(UPDATED_NOM_SEMESTRE)
            .matriculeRel(UPDATED_MATRICULE_REL);
        return releveNote;
    }

    @BeforeEach
    public void initTest() {
        releveNote = createEntity(em);
    }

    @Test
    @Transactional
    void createReleveNote() throws Exception {
        int databaseSizeBeforeCreate = releveNoteRepository.findAll().size();
        // Create the ReleveNote
        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isCreated());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeCreate + 1);
        ReleveNote testReleveNote = releveNoteList.get(releveNoteList.size() - 1);
        assertThat(testReleveNote.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testReleveNote.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testReleveNote.getApreciation()).isEqualTo(DEFAULT_APRECIATION);
        assertThat(testReleveNote.getMoyenne()).isEqualTo(DEFAULT_MOYENNE);
        assertThat(testReleveNote.getMoyenneGenerale()).isEqualTo(DEFAULT_MOYENNE_GENERALE);
        assertThat(testReleveNote.getRang()).isEqualTo(DEFAULT_RANG);
        assertThat(testReleveNote.getNoteConduite()).isEqualTo(DEFAULT_NOTE_CONDUITE);
        assertThat(testReleveNote.getNomSemestre()).isEqualTo(DEFAULT_NOM_SEMESTRE);
        assertThat(testReleveNote.getMatriculeRel()).isEqualTo(DEFAULT_MATRICULE_REL);
    }

    @Test
    @Transactional
    void createReleveNoteWithExistingId() throws Exception {
        // Create the ReleveNote with an existing ID
        releveNote.setId(1L);

        int databaseSizeBeforeCreate = releveNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = releveNoteRepository.findAll().size();
        // set the field null
        releveNote.setAnnee(null);

        // Create the ReleveNote, which fails.

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isBadRequest());

        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = releveNoteRepository.findAll().size();
        // set the field null
        releveNote.setEtat(null);

        // Create the ReleveNote, which fails.

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isBadRequest());

        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMoyenneIsRequired() throws Exception {
        int databaseSizeBeforeTest = releveNoteRepository.findAll().size();
        // set the field null
        releveNote.setMoyenne(null);

        // Create the ReleveNote, which fails.

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isBadRequest());

        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReleveNotes() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        // Get all the releveNoteList
        restReleveNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releveNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].apreciation").value(hasItem(DEFAULT_APRECIATION)))
            .andExpect(jsonPath("$.[*].moyenne").value(hasItem(DEFAULT_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].moyenneGenerale").value(hasItem(DEFAULT_MOYENNE_GENERALE.doubleValue())))
            .andExpect(jsonPath("$.[*].rang").value(hasItem(DEFAULT_RANG)))
            .andExpect(jsonPath("$.[*].noteConduite").value(hasItem(DEFAULT_NOTE_CONDUITE)))
            .andExpect(jsonPath("$.[*].nomSemestre").value(hasItem(DEFAULT_NOM_SEMESTRE.toString())))
            .andExpect(jsonPath("$.[*].matriculeRel").value(hasItem(DEFAULT_MATRICULE_REL)));
    }

    @Test
    @Transactional
    void getReleveNote() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        // Get the releveNote
        restReleveNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, releveNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releveNote.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.apreciation").value(DEFAULT_APRECIATION))
            .andExpect(jsonPath("$.moyenne").value(DEFAULT_MOYENNE.doubleValue()))
            .andExpect(jsonPath("$.moyenneGenerale").value(DEFAULT_MOYENNE_GENERALE.doubleValue()))
            .andExpect(jsonPath("$.rang").value(DEFAULT_RANG))
            .andExpect(jsonPath("$.noteConduite").value(DEFAULT_NOTE_CONDUITE))
            .andExpect(jsonPath("$.nomSemestre").value(DEFAULT_NOM_SEMESTRE.toString()))
            .andExpect(jsonPath("$.matriculeRel").value(DEFAULT_MATRICULE_REL));
    }

    @Test
    @Transactional
    void getNonExistingReleveNote() throws Exception {
        // Get the releveNote
        restReleveNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReleveNote() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();

        // Update the releveNote
        ReleveNote updatedReleveNote = releveNoteRepository.findById(releveNote.getId()).get();
        // Disconnect from session so that the updates on updatedReleveNote are not directly saved in db
        em.detach(updatedReleveNote);
        updatedReleveNote
            .annee(UPDATED_ANNEE)
            .etat(UPDATED_ETAT)
            .apreciation(UPDATED_APRECIATION)
            .moyenne(UPDATED_MOYENNE)
            .moyenneGenerale(UPDATED_MOYENNE_GENERALE)
            .rang(UPDATED_RANG)
            .noteConduite(UPDATED_NOTE_CONDUITE)
            .nomSemestre(UPDATED_NOM_SEMESTRE)
            .matriculeRel(UPDATED_MATRICULE_REL);

        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReleveNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReleveNote))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
        ReleveNote testReleveNote = releveNoteList.get(releveNoteList.size() - 1);
        assertThat(testReleveNote.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testReleveNote.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testReleveNote.getApreciation()).isEqualTo(UPDATED_APRECIATION);
        assertThat(testReleveNote.getMoyenne()).isEqualTo(UPDATED_MOYENNE);
        assertThat(testReleveNote.getMoyenneGenerale()).isEqualTo(UPDATED_MOYENNE_GENERALE);
        assertThat(testReleveNote.getRang()).isEqualTo(UPDATED_RANG);
        assertThat(testReleveNote.getNoteConduite()).isEqualTo(UPDATED_NOTE_CONDUITE);
        assertThat(testReleveNote.getNomSemestre()).isEqualTo(UPDATED_NOM_SEMESTRE);
        assertThat(testReleveNote.getMatriculeRel()).isEqualTo(UPDATED_MATRICULE_REL);
    }

    @Test
    @Transactional
    void putNonExistingReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releveNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releveNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releveNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleveNoteWithPatch() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();

        // Update the releveNote using partial update
        ReleveNote partialUpdatedReleveNote = new ReleveNote();
        partialUpdatedReleveNote.setId(releveNote.getId());

        partialUpdatedReleveNote
            .annee(UPDATED_ANNEE)
            .etat(UPDATED_ETAT)
            .apreciation(UPDATED_APRECIATION)
            .moyenne(UPDATED_MOYENNE)
            .moyenneGenerale(UPDATED_MOYENNE_GENERALE)
            .rang(UPDATED_RANG)
            .noteConduite(UPDATED_NOTE_CONDUITE);

        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveNote))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
        ReleveNote testReleveNote = releveNoteList.get(releveNoteList.size() - 1);
        assertThat(testReleveNote.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testReleveNote.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testReleveNote.getApreciation()).isEqualTo(UPDATED_APRECIATION);
        assertThat(testReleveNote.getMoyenne()).isEqualTo(UPDATED_MOYENNE);
        assertThat(testReleveNote.getMoyenneGenerale()).isEqualTo(UPDATED_MOYENNE_GENERALE);
        assertThat(testReleveNote.getRang()).isEqualTo(UPDATED_RANG);
        assertThat(testReleveNote.getNoteConduite()).isEqualTo(UPDATED_NOTE_CONDUITE);
        assertThat(testReleveNote.getNomSemestre()).isEqualTo(DEFAULT_NOM_SEMESTRE);
        assertThat(testReleveNote.getMatriculeRel()).isEqualTo(DEFAULT_MATRICULE_REL);
    }

    @Test
    @Transactional
    void fullUpdateReleveNoteWithPatch() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();

        // Update the releveNote using partial update
        ReleveNote partialUpdatedReleveNote = new ReleveNote();
        partialUpdatedReleveNote.setId(releveNote.getId());

        partialUpdatedReleveNote
            .annee(UPDATED_ANNEE)
            .etat(UPDATED_ETAT)
            .apreciation(UPDATED_APRECIATION)
            .moyenne(UPDATED_MOYENNE)
            .moyenneGenerale(UPDATED_MOYENNE_GENERALE)
            .rang(UPDATED_RANG)
            .noteConduite(UPDATED_NOTE_CONDUITE)
            .nomSemestre(UPDATED_NOM_SEMESTRE)
            .matriculeRel(UPDATED_MATRICULE_REL);

        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveNote))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
        ReleveNote testReleveNote = releveNoteList.get(releveNoteList.size() - 1);
        assertThat(testReleveNote.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testReleveNote.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testReleveNote.getApreciation()).isEqualTo(UPDATED_APRECIATION);
        assertThat(testReleveNote.getMoyenne()).isEqualTo(UPDATED_MOYENNE);
        assertThat(testReleveNote.getMoyenneGenerale()).isEqualTo(UPDATED_MOYENNE_GENERALE);
        assertThat(testReleveNote.getRang()).isEqualTo(UPDATED_RANG);
        assertThat(testReleveNote.getNoteConduite()).isEqualTo(UPDATED_NOTE_CONDUITE);
        assertThat(testReleveNote.getNomSemestre()).isEqualTo(UPDATED_NOM_SEMESTRE);
        assertThat(testReleveNote.getMatriculeRel()).isEqualTo(UPDATED_MATRICULE_REL);
    }

    @Test
    @Transactional
    void patchNonExistingReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releveNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releveNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releveNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReleveNote() throws Exception {
        int databaseSizeBeforeUpdate = releveNoteRepository.findAll().size();
        releveNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(releveNote))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveNote in the database
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReleveNote() throws Exception {
        // Initialize the database
        releveNoteRepository.saveAndFlush(releveNote);

        int databaseSizeBeforeDelete = releveNoteRepository.findAll().size();

        // Delete the releveNote
        restReleveNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, releveNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleveNote> releveNoteList = releveNoteRepository.findAll();
        assertThat(releveNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
