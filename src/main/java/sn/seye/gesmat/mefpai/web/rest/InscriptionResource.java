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
import sn.seye.gesmat.mefpai.domain.Inscription;
import sn.seye.gesmat.mefpai.repository.InscriptionRepository;
import sn.seye.gesmat.mefpai.service.InscriptionService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Inscription}.
 */
@RestController
@RequestMapping("/api")
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "inscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionService inscriptionService;

    private final InscriptionRepository inscriptionRepository;

    public InscriptionResource(InscriptionService inscriptionService, InscriptionRepository inscriptionRepository) {
        this.inscriptionService = inscriptionService;
        this.inscriptionRepository = inscriptionRepository;
    }

    /**
     * {@code POST  /inscriptions} : Create a new inscription.
     *
     * @param inscription the inscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscription, or with status {@code 400 (Bad Request)} if the inscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscriptions")
    public ResponseEntity<Inscription> createInscription(@Valid @RequestBody Inscription inscription) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscription);
        if (inscription.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inscription result = inscriptionService.save(inscription);
        return ResponseEntity
            .created(new URI("/api/inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscriptions/:id} : Updates an existing inscription.
     *
     * @param id the id of the inscription to save.
     * @param inscription the inscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscription,
     * or with status {@code 400 (Bad Request)} if the inscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscriptions/{id}")
    public ResponseEntity<Inscription> updateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Inscription inscription
    ) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}, {}", id, inscription);
        if (inscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inscription result = inscriptionService.save(inscription);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscription.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inscriptions/:id} : Partial updates given fields of an existing inscription, field will ignore if it is null
     *
     * @param id the id of the inscription to save.
     * @param inscription the inscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscription,
     * or with status {@code 400 (Bad Request)} if the inscription is not valid,
     * or with status {@code 404 (Not Found)} if the inscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inscription> partialUpdateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Inscription inscription
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inscription partially : {}, {}", id, inscription);
        if (inscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inscription> result = inscriptionService.partialUpdate(inscription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscription.getId().toString())
        );
    }

    /**
     * {@code GET  /inscriptions} : get all the inscriptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptions in body.
     */
    @GetMapping("/inscriptions")
    public ResponseEntity<List<Inscription>> getAllInscriptions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Inscriptions");
        Page<Inscription> page;
        if (eagerload) {
            page = inscriptionService.findAllWithEagerRelationships(pageable);
        } else {
            page = inscriptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscriptions/:id} : get the "id" inscription.
     *
     * @param id the id of the inscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscriptions/{id}")
    public ResponseEntity<Inscription> getInscription(@PathVariable Long id) {
        log.debug("REST request to get Inscription : {}", id);
        Optional<Inscription> inscription = inscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscription);
    }

    /**
     * {@code DELETE  /inscriptions/:id} : delete the "id" inscription.
     *
     * @param id the id of the inscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscriptions/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
