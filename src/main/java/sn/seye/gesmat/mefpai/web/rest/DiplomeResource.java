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
import sn.seye.gesmat.mefpai.domain.Diplome;
import sn.seye.gesmat.mefpai.repository.DiplomeRepository;
import sn.seye.gesmat.mefpai.service.DiplomeService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Diplome}.
 */
@RestController
@RequestMapping("/api")
public class DiplomeResource {

    private final Logger log = LoggerFactory.getLogger(DiplomeResource.class);

    private static final String ENTITY_NAME = "diplome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiplomeService diplomeService;

    private final DiplomeRepository diplomeRepository;

    public DiplomeResource(DiplomeService diplomeService, DiplomeRepository diplomeRepository) {
        this.diplomeService = diplomeService;
        this.diplomeRepository = diplomeRepository;
    }

    /**
     * {@code POST  /diplomes} : Create a new diplome.
     *
     * @param diplome the diplome to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diplome, or with status {@code 400 (Bad Request)} if the diplome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diplomes")
    public ResponseEntity<Diplome> createDiplome(@Valid @RequestBody Diplome diplome) throws URISyntaxException {
        log.debug("REST request to save Diplome : {}", diplome);
        if (diplome.getId() != null) {
            throw new BadRequestAlertException("A new diplome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Diplome result = diplomeService.save(diplome);
        return ResponseEntity
            .created(new URI("/api/diplomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diplomes/:id} : Updates an existing diplome.
     *
     * @param id the id of the diplome to save.
     * @param diplome the diplome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diplome,
     * or with status {@code 400 (Bad Request)} if the diplome is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diplome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diplomes/{id}")
    public ResponseEntity<Diplome> updateDiplome(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Diplome diplome
    ) throws URISyntaxException {
        log.debug("REST request to update Diplome : {}, {}", id, diplome);
        if (diplome.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diplome.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diplomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Diplome result = diplomeService.save(diplome);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diplome.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /diplomes/:id} : Partial updates given fields of an existing diplome, field will ignore if it is null
     *
     * @param id the id of the diplome to save.
     * @param diplome the diplome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diplome,
     * or with status {@code 400 (Bad Request)} if the diplome is not valid,
     * or with status {@code 404 (Not Found)} if the diplome is not found,
     * or with status {@code 500 (Internal Server Error)} if the diplome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diplomes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Diplome> partialUpdateDiplome(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Diplome diplome
    ) throws URISyntaxException {
        log.debug("REST request to partial update Diplome partially : {}, {}", id, diplome);
        if (diplome.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diplome.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diplomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Diplome> result = diplomeService.partialUpdate(diplome);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diplome.getId().toString())
        );
    }

    /**
     * {@code GET  /diplomes} : get all the diplomes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diplomes in body.
     */
    @GetMapping("/diplomes")
    public ResponseEntity<List<Diplome>> getAllDiplomes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Diplomes");
        Page<Diplome> page;
        if (eagerload) {
            page = diplomeService.findAllWithEagerRelationships(pageable);
        } else {
            page = diplomeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /diplomes/:id} : get the "id" diplome.
     *
     * @param id the id of the diplome to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diplome, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diplomes/{id}")
    public ResponseEntity<Diplome> getDiplome(@PathVariable Long id) {
        log.debug("REST request to get Diplome : {}", id);
        Optional<Diplome> diplome = diplomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diplome);
    }

    /**
     * {@code DELETE  /diplomes/:id} : delete the "id" diplome.
     *
     * @param id the id of the diplome to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diplomes/{id}")
    public ResponseEntity<Void> deleteDiplome(@PathVariable Long id) {
        log.debug("REST request to delete Diplome : {}", id);
        diplomeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
