package sn.seye.gesmat.mefpai.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import sn.seye.gesmat.mefpai.domain.DemandeMatEtab;
import sn.seye.gesmat.mefpai.repository.DemandeMatEtabRepository;
import sn.seye.gesmat.mefpai.service.DemandeMatEtabService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.DemandeMatEtab}.
 */
@RestController
@RequestMapping("/api")
public class DemandeMatEtabResource {

    private final Logger log = LoggerFactory.getLogger(DemandeMatEtabResource.class);

    private static final String ENTITY_NAME = "demandeMatEtab";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeMatEtabService demandeMatEtabService;

    private final DemandeMatEtabRepository demandeMatEtabRepository;

    public DemandeMatEtabResource(DemandeMatEtabService demandeMatEtabService, DemandeMatEtabRepository demandeMatEtabRepository) {
        this.demandeMatEtabService = demandeMatEtabService;
        this.demandeMatEtabRepository = demandeMatEtabRepository;
    }

    /**
     * {@code POST  /demande-mat-etabs} : Create a new demandeMatEtab.
     *
     * @param demandeMatEtab the demandeMatEtab to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeMatEtab, or with status {@code 400 (Bad Request)} if the demandeMatEtab has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-mat-etabs")
    public ResponseEntity<DemandeMatEtab> createDemandeMatEtab(@RequestBody DemandeMatEtab demandeMatEtab) throws URISyntaxException {
        log.debug("REST request to save DemandeMatEtab : {}", demandeMatEtab);
        if (demandeMatEtab.getId() != null) {
            throw new BadRequestAlertException("A new demandeMatEtab cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeMatEtab result = demandeMatEtabService.save(demandeMatEtab);
        return ResponseEntity
            .created(new URI("/api/demande-mat-etabs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-mat-etabs/:id} : Updates an existing demandeMatEtab.
     *
     * @param id the id of the demandeMatEtab to save.
     * @param demandeMatEtab the demandeMatEtab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatEtab,
     * or with status {@code 400 (Bad Request)} if the demandeMatEtab is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatEtab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-mat-etabs/{id}")
    public ResponseEntity<DemandeMatEtab> updateDemandeMatEtab(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeMatEtab demandeMatEtab
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeMatEtab : {}, {}", id, demandeMatEtab);
        if (demandeMatEtab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatEtab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatEtabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeMatEtab result = demandeMatEtabService.save(demandeMatEtab);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeMatEtab.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-mat-etabs/:id} : Partial updates given fields of an existing demandeMatEtab, field will ignore if it is null
     *
     * @param id the id of the demandeMatEtab to save.
     * @param demandeMatEtab the demandeMatEtab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatEtab,
     * or with status {@code 400 (Bad Request)} if the demandeMatEtab is not valid,
     * or with status {@code 404 (Not Found)} if the demandeMatEtab is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatEtab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-mat-etabs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeMatEtab> partialUpdateDemandeMatEtab(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeMatEtab demandeMatEtab
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeMatEtab partially : {}, {}", id, demandeMatEtab);
        if (demandeMatEtab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatEtab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatEtabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeMatEtab> result = demandeMatEtabService.partialUpdate(demandeMatEtab);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeMatEtab.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-mat-etabs} : get all the demandeMatEtabs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeMatEtabs in body.
     */
    @GetMapping("/demande-mat-etabs")
    public ResponseEntity<List<DemandeMatEtab>> getAllDemandeMatEtabs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DemandeMatEtabs");
        Page<DemandeMatEtab> page;
        if (eagerload) {
            page = demandeMatEtabService.findAllWithEagerRelationships(pageable);
        } else {
            page = demandeMatEtabService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-mat-etabs/:id} : get the "id" demandeMatEtab.
     *
     * @param id the id of the demandeMatEtab to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeMatEtab, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-mat-etabs/{id}")
    public ResponseEntity<DemandeMatEtab> getDemandeMatEtab(@PathVariable Long id) {
        log.debug("REST request to get DemandeMatEtab : {}", id);
        Optional<DemandeMatEtab> demandeMatEtab = demandeMatEtabService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeMatEtab);
    }

    /**
     * {@code DELETE  /demande-mat-etabs/:id} : delete the "id" demandeMatEtab.
     *
     * @param id the id of the demandeMatEtab to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-mat-etabs/{id}")
    public ResponseEntity<Void> deleteDemandeMatEtab(@PathVariable Long id) {
        log.debug("REST request to delete DemandeMatEtab : {}", id);
        demandeMatEtabService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
