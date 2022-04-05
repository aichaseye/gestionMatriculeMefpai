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
import sn.seye.gesmat.mefpai.domain.Departement;
import sn.seye.gesmat.mefpai.repository.DepartementRepository;
import sn.seye.gesmat.mefpai.service.DepartementService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Departement}.
 */
@RestController
@RequestMapping("/api")
public class DepartementResource {

    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementService departementService;

    private final DepartementRepository departementRepository;

    public DepartementResource(DepartementService departementService, DepartementRepository departementRepository) {
        this.departementService = departementService;
        this.departementRepository = departementRepository;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departement the departement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departement, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departements")
    public ResponseEntity<Departement> createDepartement(@Valid @RequestBody Departement departement) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departement);
        if (departement.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Departement result = departementService.save(departement);
        return ResponseEntity
            .created(new URI("/api/departements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departements/:id} : Updates an existing departement.
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departements/{id}")
    public ResponseEntity<Departement> updateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to update Departement : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Departement result = departementService.save(departement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departements/:id} : Partial updates given fields of an existing departement, field will ignore if it is null
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 404 (Not Found)} if the departement is not found,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Departement> partialUpdateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departement partially : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Departement> result = departementService.partialUpdate(departement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departement.getId().toString())
        );
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("/departements")
    public ResponseEntity<List<Departement>> getAllDepartements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Departements");
        Page<Departement> page;
        if (eagerload) {
            page = departementService.findAllWithEagerRelationships(pageable);
        } else {
            page = departementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departements/{id}")
    public ResponseEntity<Departement> getDepartement(@PathVariable Long id) {
        log.debug("REST request to get Departement : {}", id);
        Optional<Departement> departement = departementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departement);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departements/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        log.debug("REST request to delete Departement : {}", id);
        departementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
