package sn.seye.gesmat.mefpai.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.seye.gesmat.mefpai.domain.CarteScolaire;
import sn.seye.gesmat.mefpai.repository.CarteScolaireRepository;
import sn.seye.gesmat.mefpai.service.CarteScolaireService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.CarteScolaire}.
 */
@RestController
@RequestMapping("/api")
public class CarteScolaireResource {

    private final Logger log = LoggerFactory.getLogger(CarteScolaireResource.class);

    private static final String ENTITY_NAME = "carteScolaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarteScolaireService carteScolaireService;

    private final CarteScolaireRepository carteScolaireRepository;

    public CarteScolaireResource(CarteScolaireService carteScolaireService, CarteScolaireRepository carteScolaireRepository) {
        this.carteScolaireService = carteScolaireService;
        this.carteScolaireRepository = carteScolaireRepository;
    }

    /**
     * {@code POST  /carte-scolaires} : Create a new carteScolaire.
     *
     * @param carteScolaire the carteScolaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carteScolaire, or with status {@code 400 (Bad Request)} if the carteScolaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carte-scolaires")
    public ResponseEntity<CarteScolaire> createCarteScolaire(@Valid @RequestBody CarteScolaire carteScolaire) throws URISyntaxException {
        log.debug("REST request to save CarteScolaire : {}", carteScolaire);
        if (carteScolaire.getId() != null) {
            throw new BadRequestAlertException("A new carteScolaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarteScolaire result = carteScolaireService.save(carteScolaire);
        return ResponseEntity
            .created(new URI("/api/carte-scolaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carte-scolaires/:id} : Updates an existing carteScolaire.
     *
     * @param id the id of the carteScolaire to save.
     * @param carteScolaire the carteScolaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteScolaire,
     * or with status {@code 400 (Bad Request)} if the carteScolaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carteScolaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carte-scolaires/{id}")
    public ResponseEntity<CarteScolaire> updateCarteScolaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarteScolaire carteScolaire
    ) throws URISyntaxException {
        log.debug("REST request to update CarteScolaire : {}, {}", id, carteScolaire);
        if (carteScolaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteScolaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteScolaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarteScolaire result = carteScolaireService.save(carteScolaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carteScolaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carte-scolaires/:id} : Partial updates given fields of an existing carteScolaire, field will ignore if it is null
     *
     * @param id the id of the carteScolaire to save.
     * @param carteScolaire the carteScolaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteScolaire,
     * or with status {@code 400 (Bad Request)} if the carteScolaire is not valid,
     * or with status {@code 404 (Not Found)} if the carteScolaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the carteScolaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carte-scolaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarteScolaire> partialUpdateCarteScolaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarteScolaire carteScolaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarteScolaire partially : {}, {}", id, carteScolaire);
        if (carteScolaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteScolaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteScolaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarteScolaire> result = carteScolaireService.partialUpdate(carteScolaire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carteScolaire.getId().toString())
        );
    }

    /**
     * {@code GET  /carte-scolaires} : get all the carteScolaires.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carteScolaires in body.
     */
    @GetMapping("/carte-scolaires")
    public ResponseEntity<List<CarteScolaire>> getAllCarteScolaires(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CarteScolaires");
        Page<CarteScolaire> page;
        if (eagerload) {
            page = carteScolaireService.findAllWithEagerRelationships(pageable);
        } else {
            page = carteScolaireService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /carte-scolaires/:id} : get the "id" carteScolaire.
     *
     * @param id the id of the carteScolaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carteScolaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carte-scolaires/{id}")
    public ResponseEntity<CarteScolaire> getCarteScolaire(@PathVariable Long id) {
        log.debug("REST request to get CarteScolaire : {}", id);
        Optional<CarteScolaire> carteScolaire = carteScolaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carteScolaire);
    }

    /**
     * {@code DELETE  /carte-scolaires/:id} : delete the "id" carteScolaire.
     *
     * @param id the id of the carteScolaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carte-scolaires/{id}")
    public ResponseEntity<Void> deleteCarteScolaire(@PathVariable Long id) {
        log.debug("REST request to delete CarteScolaire : {}", id);
        carteScolaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
