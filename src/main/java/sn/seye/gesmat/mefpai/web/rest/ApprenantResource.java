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
import sn.seye.gesmat.mefpai.domain.Apprenant;
import sn.seye.gesmat.mefpai.repository.ApprenantRepository;
import sn.seye.gesmat.mefpai.service.ApprenantService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Apprenant}.
 */
@RestController
@RequestMapping("/api")
public class ApprenantResource {

    private final Logger log = LoggerFactory.getLogger(ApprenantResource.class);

    private static final String ENTITY_NAME = "apprenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprenantService apprenantService;

    private final ApprenantRepository apprenantRepository;

    public ApprenantResource(ApprenantService apprenantService, ApprenantRepository apprenantRepository) {
        this.apprenantService = apprenantService;
        this.apprenantRepository = apprenantRepository;
    }

    /**
     * {@code POST  /apprenants} : Create a new apprenant.
     *
     * @param apprenant the apprenant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apprenant, or with status {@code 400 (Bad Request)} if the apprenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apprenants")
    public ResponseEntity<Apprenant> createApprenant(@Valid @RequestBody Apprenant apprenant) throws URISyntaxException {
        log.debug("REST request to save Apprenant : {}", apprenant);
        if (apprenant.getId() != null) {
            throw new BadRequestAlertException("A new apprenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apprenant result = apprenantService.save(apprenant);
        return ResponseEntity
            .created(new URI("/api/apprenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apprenants/:id} : Updates an existing apprenant.
     *
     * @param id the id of the apprenant to save.
     * @param apprenant the apprenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apprenant,
     * or with status {@code 400 (Bad Request)} if the apprenant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apprenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apprenants/{id}")
    public ResponseEntity<Apprenant> updateApprenant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Apprenant apprenant
    ) throws URISyntaxException {
        log.debug("REST request to update Apprenant : {}, {}", id, apprenant);
        if (apprenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apprenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apprenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apprenant result = apprenantService.save(apprenant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apprenant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apprenants/:id} : Partial updates given fields of an existing apprenant, field will ignore if it is null
     *
     * @param id the id of the apprenant to save.
     * @param apprenant the apprenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apprenant,
     * or with status {@code 400 (Bad Request)} if the apprenant is not valid,
     * or with status {@code 404 (Not Found)} if the apprenant is not found,
     * or with status {@code 500 (Internal Server Error)} if the apprenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apprenants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apprenant> partialUpdateApprenant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Apprenant apprenant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apprenant partially : {}, {}", id, apprenant);
        if (apprenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apprenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apprenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apprenant> result = apprenantService.partialUpdate(apprenant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apprenant.getId().toString())
        );
    }

    /**
     * {@code GET  /apprenants} : get all the apprenants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apprenants in body.
     */
    @GetMapping("/apprenants")
    public ResponseEntity<List<Apprenant>> getAllApprenants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Apprenants");
        Page<Apprenant> page;
        if (eagerload) {
            page = apprenantService.findAllWithEagerRelationships(pageable);
        } else {
            page = apprenantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apprenants/:id} : get the "id" apprenant.
     *
     * @param id the id of the apprenant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apprenant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apprenants/{id}")
    public ResponseEntity<Apprenant> getApprenant(@PathVariable Long id) {
        log.debug("REST request to get Apprenant : {}", id);
        Optional<Apprenant> apprenant = apprenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apprenant);
    }

    /**
     * {@code DELETE  /apprenants/:id} : delete the "id" apprenant.
     *
     * @param id the id of the apprenant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apprenants/{id}")
    public ResponseEntity<Void> deleteApprenant(@PathVariable Long id) {
        log.debug("REST request to delete Apprenant : {}", id);
        apprenantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
