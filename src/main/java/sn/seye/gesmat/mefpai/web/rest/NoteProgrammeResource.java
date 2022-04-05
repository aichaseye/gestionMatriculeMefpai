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
import sn.seye.gesmat.mefpai.domain.NoteProgramme;
import sn.seye.gesmat.mefpai.repository.NoteProgrammeRepository;
import sn.seye.gesmat.mefpai.service.NoteProgrammeService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.NoteProgramme}.
 */
@RestController
@RequestMapping("/api")
public class NoteProgrammeResource {

    private final Logger log = LoggerFactory.getLogger(NoteProgrammeResource.class);

    private static final String ENTITY_NAME = "noteProgramme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteProgrammeService noteProgrammeService;

    private final NoteProgrammeRepository noteProgrammeRepository;

    public NoteProgrammeResource(NoteProgrammeService noteProgrammeService, NoteProgrammeRepository noteProgrammeRepository) {
        this.noteProgrammeService = noteProgrammeService;
        this.noteProgrammeRepository = noteProgrammeRepository;
    }

    /**
     * {@code POST  /note-programmes} : Create a new noteProgramme.
     *
     * @param noteProgramme the noteProgramme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteProgramme, or with status {@code 400 (Bad Request)} if the noteProgramme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/note-programmes")
    public ResponseEntity<NoteProgramme> createNoteProgramme(@RequestBody NoteProgramme noteProgramme) throws URISyntaxException {
        log.debug("REST request to save NoteProgramme : {}", noteProgramme);
        if (noteProgramme.getId() != null) {
            throw new BadRequestAlertException("A new noteProgramme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NoteProgramme result = noteProgrammeService.save(noteProgramme);
        return ResponseEntity
            .created(new URI("/api/note-programmes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /note-programmes/:id} : Updates an existing noteProgramme.
     *
     * @param id the id of the noteProgramme to save.
     * @param noteProgramme the noteProgramme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteProgramme,
     * or with status {@code 400 (Bad Request)} if the noteProgramme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteProgramme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/note-programmes/{id}")
    public ResponseEntity<NoteProgramme> updateNoteProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NoteProgramme noteProgramme
    ) throws URISyntaxException {
        log.debug("REST request to update NoteProgramme : {}, {}", id, noteProgramme);
        if (noteProgramme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteProgramme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteProgrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NoteProgramme result = noteProgrammeService.save(noteProgramme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteProgramme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /note-programmes/:id} : Partial updates given fields of an existing noteProgramme, field will ignore if it is null
     *
     * @param id the id of the noteProgramme to save.
     * @param noteProgramme the noteProgramme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteProgramme,
     * or with status {@code 400 (Bad Request)} if the noteProgramme is not valid,
     * or with status {@code 404 (Not Found)} if the noteProgramme is not found,
     * or with status {@code 500 (Internal Server Error)} if the noteProgramme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/note-programmes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NoteProgramme> partialUpdateNoteProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NoteProgramme noteProgramme
    ) throws URISyntaxException {
        log.debug("REST request to partial update NoteProgramme partially : {}, {}", id, noteProgramme);
        if (noteProgramme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteProgramme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteProgrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NoteProgramme> result = noteProgrammeService.partialUpdate(noteProgramme);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteProgramme.getId().toString())
        );
    }

    /**
     * {@code GET  /note-programmes} : get all the noteProgrammes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of noteProgrammes in body.
     */
    @GetMapping("/note-programmes")
    public ResponseEntity<List<NoteProgramme>> getAllNoteProgrammes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of NoteProgrammes");
        Page<NoteProgramme> page;
        if (eagerload) {
            page = noteProgrammeService.findAllWithEagerRelationships(pageable);
        } else {
            page = noteProgrammeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /note-programmes/:id} : get the "id" noteProgramme.
     *
     * @param id the id of the noteProgramme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteProgramme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/note-programmes/{id}")
    public ResponseEntity<NoteProgramme> getNoteProgramme(@PathVariable Long id) {
        log.debug("REST request to get NoteProgramme : {}", id);
        Optional<NoteProgramme> noteProgramme = noteProgrammeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteProgramme);
    }

    /**
     * {@code DELETE  /note-programmes/:id} : delete the "id" noteProgramme.
     *
     * @param id the id of the noteProgramme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/note-programmes/{id}")
    public ResponseEntity<Void> deleteNoteProgramme(@PathVariable Long id) {
        log.debug("REST request to delete NoteProgramme : {}", id);
        noteProgrammeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
