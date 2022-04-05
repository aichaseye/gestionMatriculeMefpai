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
import sn.seye.gesmat.mefpai.domain.CarteScolaire;
import sn.seye.gesmat.mefpai.repository.CarteScolaireRepository;

/**
 * Integration tests for the {@link CarteScolaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarteScolaireResourceIT {

    private static final Integer DEFAULT_LONGUER = 1;
    private static final Integer UPDATED_LONGUER = 2;

    private static final Integer DEFAULT_LARGEUR = 1;
    private static final Integer UPDATED_LARGEUR = 2;

    private static final Integer DEFAULT_DUREE_VALIDITE = 1;
    private static final Integer UPDATED_DUREE_VALIDITE = 2;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MATRICULE_CART = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_CART = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/carte-scolaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarteScolaireRepository carteScolaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarteScolaireMockMvc;

    private CarteScolaire carteScolaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteScolaire createEntity(EntityManager em) {
        CarteScolaire carteScolaire = new CarteScolaire()
            .longuer(DEFAULT_LONGUER)
            .largeur(DEFAULT_LARGEUR)
            .dureeValidite(DEFAULT_DUREE_VALIDITE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateFin(DEFAULT_DATE_FIN)
            .matriculeCart(DEFAULT_MATRICULE_CART);
        return carteScolaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteScolaire createUpdatedEntity(EntityManager em) {
        CarteScolaire carteScolaire = new CarteScolaire()
            .longuer(UPDATED_LONGUER)
            .largeur(UPDATED_LARGEUR)
            .dureeValidite(UPDATED_DUREE_VALIDITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN)
            .matriculeCart(UPDATED_MATRICULE_CART);
        return carteScolaire;
    }

    @BeforeEach
    public void initTest() {
        carteScolaire = createEntity(em);
    }

    @Test
    @Transactional
    void createCarteScolaire() throws Exception {
        int databaseSizeBeforeCreate = carteScolaireRepository.findAll().size();
        // Create the CarteScolaire
        restCarteScolaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteScolaire)))
            .andExpect(status().isCreated());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeCreate + 1);
        CarteScolaire testCarteScolaire = carteScolaireList.get(carteScolaireList.size() - 1);
        assertThat(testCarteScolaire.getLonguer()).isEqualTo(DEFAULT_LONGUER);
        assertThat(testCarteScolaire.getLargeur()).isEqualTo(DEFAULT_LARGEUR);
        assertThat(testCarteScolaire.getDureeValidite()).isEqualTo(DEFAULT_DUREE_VALIDITE);
        assertThat(testCarteScolaire.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testCarteScolaire.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCarteScolaire.getMatriculeCart()).isEqualTo(DEFAULT_MATRICULE_CART);
    }

    @Test
    @Transactional
    void createCarteScolaireWithExistingId() throws Exception {
        // Create the CarteScolaire with an existing ID
        carteScolaire.setId(1L);

        int databaseSizeBeforeCreate = carteScolaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarteScolaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteScolaire)))
            .andExpect(status().isBadRequest());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLonguerIsRequired() throws Exception {
        int databaseSizeBeforeTest = carteScolaireRepository.findAll().size();
        // set the field null
        carteScolaire.setLonguer(null);

        // Create the CarteScolaire, which fails.

        restCarteScolaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteScolaire)))
            .andExpect(status().isBadRequest());

        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLargeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = carteScolaireRepository.findAll().size();
        // set the field null
        carteScolaire.setLargeur(null);

        // Create the CarteScolaire, which fails.

        restCarteScolaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteScolaire)))
            .andExpect(status().isBadRequest());

        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarteScolaires() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        // Get all the carteScolaireList
        restCarteScolaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carteScolaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].longuer").value(hasItem(DEFAULT_LONGUER)))
            .andExpect(jsonPath("$.[*].largeur").value(hasItem(DEFAULT_LARGEUR)))
            .andExpect(jsonPath("$.[*].dureeValidite").value(hasItem(DEFAULT_DUREE_VALIDITE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].matriculeCart").value(hasItem(DEFAULT_MATRICULE_CART)));
    }

    @Test
    @Transactional
    void getCarteScolaire() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        // Get the carteScolaire
        restCarteScolaireMockMvc
            .perform(get(ENTITY_API_URL_ID, carteScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carteScolaire.getId().intValue()))
            .andExpect(jsonPath("$.longuer").value(DEFAULT_LONGUER))
            .andExpect(jsonPath("$.largeur").value(DEFAULT_LARGEUR))
            .andExpect(jsonPath("$.dureeValidite").value(DEFAULT_DUREE_VALIDITE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.matriculeCart").value(DEFAULT_MATRICULE_CART));
    }

    @Test
    @Transactional
    void getNonExistingCarteScolaire() throws Exception {
        // Get the carteScolaire
        restCarteScolaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarteScolaire() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();

        // Update the carteScolaire
        CarteScolaire updatedCarteScolaire = carteScolaireRepository.findById(carteScolaire.getId()).get();
        // Disconnect from session so that the updates on updatedCarteScolaire are not directly saved in db
        em.detach(updatedCarteScolaire);
        updatedCarteScolaire
            .longuer(UPDATED_LONGUER)
            .largeur(UPDATED_LARGEUR)
            .dureeValidite(UPDATED_DUREE_VALIDITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN)
            .matriculeCart(UPDATED_MATRICULE_CART);

        restCarteScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarteScolaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarteScolaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
        CarteScolaire testCarteScolaire = carteScolaireList.get(carteScolaireList.size() - 1);
        assertThat(testCarteScolaire.getLonguer()).isEqualTo(UPDATED_LONGUER);
        assertThat(testCarteScolaire.getLargeur()).isEqualTo(UPDATED_LARGEUR);
        assertThat(testCarteScolaire.getDureeValidite()).isEqualTo(UPDATED_DUREE_VALIDITE);
        assertThat(testCarteScolaire.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCarteScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCarteScolaire.getMatriculeCart()).isEqualTo(UPDATED_MATRICULE_CART);
    }

    @Test
    @Transactional
    void putNonExistingCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carteScolaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carteScolaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carteScolaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteScolaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarteScolaireWithPatch() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();

        // Update the carteScolaire using partial update
        CarteScolaire partialUpdatedCarteScolaire = new CarteScolaire();
        partialUpdatedCarteScolaire.setId(carteScolaire.getId());

        partialUpdatedCarteScolaire
            .longuer(UPDATED_LONGUER)
            .largeur(UPDATED_LARGEUR)
            .dureeValidite(UPDATED_DUREE_VALIDITE)
            .matriculeCart(UPDATED_MATRICULE_CART);

        restCarteScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteScolaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteScolaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
        CarteScolaire testCarteScolaire = carteScolaireList.get(carteScolaireList.size() - 1);
        assertThat(testCarteScolaire.getLonguer()).isEqualTo(UPDATED_LONGUER);
        assertThat(testCarteScolaire.getLargeur()).isEqualTo(UPDATED_LARGEUR);
        assertThat(testCarteScolaire.getDureeValidite()).isEqualTo(UPDATED_DUREE_VALIDITE);
        assertThat(testCarteScolaire.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testCarteScolaire.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCarteScolaire.getMatriculeCart()).isEqualTo(UPDATED_MATRICULE_CART);
    }

    @Test
    @Transactional
    void fullUpdateCarteScolaireWithPatch() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();

        // Update the carteScolaire using partial update
        CarteScolaire partialUpdatedCarteScolaire = new CarteScolaire();
        partialUpdatedCarteScolaire.setId(carteScolaire.getId());

        partialUpdatedCarteScolaire
            .longuer(UPDATED_LONGUER)
            .largeur(UPDATED_LARGEUR)
            .dureeValidite(UPDATED_DUREE_VALIDITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN)
            .matriculeCart(UPDATED_MATRICULE_CART);

        restCarteScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteScolaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteScolaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
        CarteScolaire testCarteScolaire = carteScolaireList.get(carteScolaireList.size() - 1);
        assertThat(testCarteScolaire.getLonguer()).isEqualTo(UPDATED_LONGUER);
        assertThat(testCarteScolaire.getLargeur()).isEqualTo(UPDATED_LARGEUR);
        assertThat(testCarteScolaire.getDureeValidite()).isEqualTo(UPDATED_DUREE_VALIDITE);
        assertThat(testCarteScolaire.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCarteScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCarteScolaire.getMatriculeCart()).isEqualTo(UPDATED_MATRICULE_CART);
    }

    @Test
    @Transactional
    void patchNonExistingCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carteScolaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carteScolaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carteScolaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarteScolaire() throws Exception {
        int databaseSizeBeforeUpdate = carteScolaireRepository.findAll().size();
        carteScolaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carteScolaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarteScolaire in the database
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarteScolaire() throws Exception {
        // Initialize the database
        carteScolaireRepository.saveAndFlush(carteScolaire);

        int databaseSizeBeforeDelete = carteScolaireRepository.findAll().size();

        // Delete the carteScolaire
        restCarteScolaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, carteScolaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarteScolaire> carteScolaireList = carteScolaireRepository.findAll();
        assertThat(carteScolaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
