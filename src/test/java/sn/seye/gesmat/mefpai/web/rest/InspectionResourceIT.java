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
import sn.seye.gesmat.mefpai.domain.Inspection;
import sn.seye.gesmat.mefpai.domain.enumeration.TypeInspection;
import sn.seye.gesmat.mefpai.repository.InspectionRepository;

/**
 * Integration tests for the {@link InspectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspectionResourceIT {

    private static final String DEFAULT_NOM_INSP = "AAAAAAAAAA";
    private static final String UPDATED_NOM_INSP = "BBBBBBBBBB";

    private static final TypeInspection DEFAULT_TYPE_INSP = TypeInspection.IA;
    private static final TypeInspection UPDATED_TYPE_INSP = TypeInspection.IEF;

    private static final Long DEFAULT_LATITUDE = 1L;
    private static final Long UPDATED_LATITUDE = 2L;

    private static final Long DEFAULT_LONGITUDE = 1L;
    private static final Long UPDATED_LONGITUDE = 2L;

    private static final String ENTITY_API_URL = "/api/inspections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InspectionRepository inspectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInspectionMockMvc;

    private Inspection inspection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createEntity(EntityManager em) {
        Inspection inspection = new Inspection()
            .nomInsp(DEFAULT_NOM_INSP)
            .typeInsp(DEFAULT_TYPE_INSP)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return inspection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createUpdatedEntity(EntityManager em) {
        Inspection inspection = new Inspection()
            .nomInsp(UPDATED_NOM_INSP)
            .typeInsp(UPDATED_TYPE_INSP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return inspection;
    }

    @BeforeEach
    public void initTest() {
        inspection = createEntity(em);
    }

    @Test
    @Transactional
    void createInspection() throws Exception {
        int databaseSizeBeforeCreate = inspectionRepository.findAll().size();
        // Create the Inspection
        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isCreated());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate + 1);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getNomInsp()).isEqualTo(DEFAULT_NOM_INSP);
        assertThat(testInspection.getTypeInsp()).isEqualTo(DEFAULT_TYPE_INSP);
        assertThat(testInspection.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testInspection.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createInspectionWithExistingId() throws Exception {
        // Create the Inspection with an existing ID
        inspection.setId(1L);

        int databaseSizeBeforeCreate = inspectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomInspIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspectionRepository.findAll().size();
        // set the field null
        inspection.setNomInsp(null);

        // Create the Inspection, which fails.

        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isBadRequest());

        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeInspIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspectionRepository.findAll().size();
        // set the field null
        inspection.setTypeInsp(null);

        // Create the Inspection, which fails.

        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isBadRequest());

        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInspections() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspection.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomInsp").value(hasItem(DEFAULT_NOM_INSP)))
            .andExpect(jsonPath("$.[*].typeInsp").value(hasItem(DEFAULT_TYPE_INSP.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())));
    }

    @Test
    @Transactional
    void getInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        // Get the inspection
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL_ID, inspection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspection.getId().intValue()))
            .andExpect(jsonPath("$.nomInsp").value(DEFAULT_NOM_INSP))
            .andExpect(jsonPath("$.typeInsp").value(DEFAULT_TYPE_INSP.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingInspection() throws Exception {
        // Get the inspection
        restInspectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection
        Inspection updatedInspection = inspectionRepository.findById(inspection.getId()).get();
        // Disconnect from session so that the updates on updatedInspection are not directly saved in db
        em.detach(updatedInspection);
        updatedInspection.nomInsp(UPDATED_NOM_INSP).typeInsp(UPDATED_TYPE_INSP).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getNomInsp()).isEqualTo(UPDATED_NOM_INSP);
        assertThat(testInspection.getTypeInsp()).isEqualTo(UPDATED_TYPE_INSP);
        assertThat(testInspection.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInspection.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection.nomInsp(UPDATED_NOM_INSP).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getNomInsp()).isEqualTo(UPDATED_NOM_INSP);
        assertThat(testInspection.getTypeInsp()).isEqualTo(DEFAULT_TYPE_INSP);
        assertThat(testInspection.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInspection.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection
            .nomInsp(UPDATED_NOM_INSP)
            .typeInsp(UPDATED_TYPE_INSP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
        assertThat(testInspection.getNomInsp()).isEqualTo(UPDATED_NOM_INSP);
        assertThat(testInspection.getTypeInsp()).isEqualTo(UPDATED_TYPE_INSP);
        assertThat(testInspection.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInspection.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().size();
        inspection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inspection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInspection() throws Exception {
        // Initialize the database
        inspectionRepository.saveAndFlush(inspection);

        int databaseSizeBeforeDelete = inspectionRepository.findAll().size();

        // Delete the inspection
        restInspectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inspection> inspectionList = inspectionRepository.findAll();
        assertThat(inspectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
