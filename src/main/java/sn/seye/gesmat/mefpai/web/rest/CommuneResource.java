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
import sn.seye.gesmat.mefpai.domain.Commune;
import sn.seye.gesmat.mefpai.repository.CommuneRepository;
import sn.seye.gesmat.mefpai.service.CommuneService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Commune}.
 */
@RestController
@RequestMapping("/api")
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    private static final String ENTITY_NAME = "commune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommuneService communeService;

    private final CommuneRepository communeRepository;

    public CommuneResource(CommuneService communeService, CommuneRepository communeRepository) {
        this.communeService = communeService;
        this.communeRepository = communeRepository;
    }

    /**
     * {@code POST  /communes} : Create a new commune.
     *
     * @param commune the commune to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commune, or with status {@code 400 (Bad Request)} if the commune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/communes")
    public ResponseEntity<Commune> createCommune(@Valid @RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", commune);
        if (commune.getId() != null) {
            throw new BadRequestAlertException("A new commune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commune result = communeService.save(commune);
        return ResponseEntity
            .created(new URI("/api/communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communes/:id} : Updates an existing commune.
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/communes/{id}")
    public ResponseEntity<Commune> updateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to update Commune : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commune result = communeService.save(commune);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commune.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /communes/:id} : Partial updates given fields of an existing commune, field will ignore if it is null
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 404 (Not Found)} if the commune is not found,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/communes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Commune> partialUpdateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commune partially : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commune> result = communeService.partialUpdate(commune);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commune.getId().toString())
        );
    }

    /**
     * {@code GET  /communes} : get all the communes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communes in body.
     */
    @GetMapping("/communes")
    public ResponseEntity<List<Commune>> getAllCommunes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Communes");
        Page<Commune> page;
        if (eagerload) {
            page = communeService.findAllWithEagerRelationships(pageable);
        } else {
            page = communeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communes/:id} : get the "id" commune.
     *
     * @param id the id of the commune to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commune, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/communes/{id}")
    public ResponseEntity<Commune> getCommune(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        Optional<Commune> commune = communeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commune);
    }

    /**
     * {@code DELETE  /communes/:id} : delete the "id" commune.
     *
     * @param id the id of the commune to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/communes/{id}")
    public ResponseEntity<Void> deleteCommune(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        communeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
