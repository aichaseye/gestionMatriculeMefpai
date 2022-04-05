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
import sn.seye.gesmat.mefpai.domain.DemandeMatApp;
import sn.seye.gesmat.mefpai.repository.DemandeMatAppRepository;
import sn.seye.gesmat.mefpai.service.DemandeMatAppService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.DemandeMatApp}.
 */
@RestController
@RequestMapping("/api")
public class DemandeMatAppResource {

    private final Logger log = LoggerFactory.getLogger(DemandeMatAppResource.class);

    private static final String ENTITY_NAME = "demandeMatApp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeMatAppService demandeMatAppService;

    private final DemandeMatAppRepository demandeMatAppRepository;

    public DemandeMatAppResource(DemandeMatAppService demandeMatAppService, DemandeMatAppRepository demandeMatAppRepository) {
        this.demandeMatAppService = demandeMatAppService;
        this.demandeMatAppRepository = demandeMatAppRepository;
    }

    /**
     * {@code POST  /demande-mat-apps} : Create a new demandeMatApp.
     *
     * @param demandeMatApp the demandeMatApp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeMatApp, or with status {@code 400 (Bad Request)} if the demandeMatApp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-mat-apps")
    public ResponseEntity<DemandeMatApp> createDemandeMatApp(@RequestBody DemandeMatApp demandeMatApp) throws URISyntaxException {
        log.debug("REST request to save DemandeMatApp : {}", demandeMatApp);
        if (demandeMatApp.getId() != null) {
            throw new BadRequestAlertException("A new demandeMatApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeMatApp result = demandeMatAppService.save(demandeMatApp);
        return ResponseEntity
            .created(new URI("/api/demande-mat-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-mat-apps/:id} : Updates an existing demandeMatApp.
     *
     * @param id the id of the demandeMatApp to save.
     * @param demandeMatApp the demandeMatApp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatApp,
     * or with status {@code 400 (Bad Request)} if the demandeMatApp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatApp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-mat-apps/{id}")
    public ResponseEntity<DemandeMatApp> updateDemandeMatApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeMatApp demandeMatApp
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeMatApp : {}, {}", id, demandeMatApp);
        if (demandeMatApp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatApp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeMatApp result = demandeMatAppService.save(demandeMatApp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeMatApp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-mat-apps/:id} : Partial updates given fields of an existing demandeMatApp, field will ignore if it is null
     *
     * @param id the id of the demandeMatApp to save.
     * @param demandeMatApp the demandeMatApp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatApp,
     * or with status {@code 400 (Bad Request)} if the demandeMatApp is not valid,
     * or with status {@code 404 (Not Found)} if the demandeMatApp is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatApp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-mat-apps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeMatApp> partialUpdateDemandeMatApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeMatApp demandeMatApp
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeMatApp partially : {}, {}", id, demandeMatApp);
        if (demandeMatApp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatApp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeMatApp> result = demandeMatAppService.partialUpdate(demandeMatApp);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeMatApp.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-mat-apps} : get all the demandeMatApps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeMatApps in body.
     */
    @GetMapping("/demande-mat-apps")
    public ResponseEntity<List<DemandeMatApp>> getAllDemandeMatApps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DemandeMatApps");
        Page<DemandeMatApp> page = demandeMatAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-mat-apps/:id} : get the "id" demandeMatApp.
     *
     * @param id the id of the demandeMatApp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeMatApp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-mat-apps/{id}")
    public ResponseEntity<DemandeMatApp> getDemandeMatApp(@PathVariable Long id) {
        log.debug("REST request to get DemandeMatApp : {}", id);
        Optional<DemandeMatApp> demandeMatApp = demandeMatAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeMatApp);
    }

    /**
     * {@code DELETE  /demande-mat-apps/:id} : delete the "id" demandeMatApp.
     *
     * @param id the id of the demandeMatApp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-mat-apps/{id}")
    public ResponseEntity<Void> deleteDemandeMatApp(@PathVariable Long id) {
        log.debug("REST request to delete DemandeMatApp : {}", id);
        demandeMatAppService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
