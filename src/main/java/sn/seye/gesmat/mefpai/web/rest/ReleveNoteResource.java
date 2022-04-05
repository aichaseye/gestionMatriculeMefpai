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
import sn.seye.gesmat.mefpai.domain.ReleveNote;
import sn.seye.gesmat.mefpai.repository.ReleveNoteRepository;
import sn.seye.gesmat.mefpai.service.ReleveNoteService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.ReleveNote}.
 */
@RestController
@RequestMapping("/api")
public class ReleveNoteResource {

    private final Logger log = LoggerFactory.getLogger(ReleveNoteResource.class);

    private static final String ENTITY_NAME = "releveNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleveNoteService releveNoteService;

    private final ReleveNoteRepository releveNoteRepository;

    public ReleveNoteResource(ReleveNoteService releveNoteService, ReleveNoteRepository releveNoteRepository) {
        this.releveNoteService = releveNoteService;
        this.releveNoteRepository = releveNoteRepository;
    }

    /**
     * {@code POST  /releve-notes} : Create a new releveNote.
     *
     * @param releveNote the releveNote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releveNote, or with status {@code 400 (Bad Request)} if the releveNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/releve-notes")
    public ResponseEntity<ReleveNote> createReleveNote(@Valid @RequestBody ReleveNote releveNote) throws URISyntaxException {
        log.debug("REST request to save ReleveNote : {}", releveNote);
        if (releveNote.getId() != null) {
            throw new BadRequestAlertException("A new releveNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleveNote result = releveNoteService.save(releveNote);
        return ResponseEntity
            .created(new URI("/api/releve-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /releve-notes/:id} : Updates an existing releveNote.
     *
     * @param id the id of the releveNote to save.
     * @param releveNote the releveNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveNote,
     * or with status {@code 400 (Bad Request)} if the releveNote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releveNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/releve-notes/{id}")
    public ResponseEntity<ReleveNote> updateReleveNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReleveNote releveNote
    ) throws URISyntaxException {
        log.debug("REST request to update ReleveNote : {}, {}", id, releveNote);
        if (releveNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReleveNote result = releveNoteService.save(releveNote);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveNote.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /releve-notes/:id} : Partial updates given fields of an existing releveNote, field will ignore if it is null
     *
     * @param id the id of the releveNote to save.
     * @param releveNote the releveNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveNote,
     * or with status {@code 400 (Bad Request)} if the releveNote is not valid,
     * or with status {@code 404 (Not Found)} if the releveNote is not found,
     * or with status {@code 500 (Internal Server Error)} if the releveNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/releve-notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleveNote> partialUpdateReleveNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReleveNote releveNote
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReleveNote partially : {}, {}", id, releveNote);
        if (releveNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleveNote> result = releveNoteService.partialUpdate(releveNote);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveNote.getId().toString())
        );
    }

    /**
     * {@code GET  /releve-notes} : get all the releveNotes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releveNotes in body.
     */
    @GetMapping("/releve-notes")
    public ResponseEntity<List<ReleveNote>> getAllReleveNotes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ReleveNotes");
        Page<ReleveNote> page;
        if (eagerload) {
            page = releveNoteService.findAllWithEagerRelationships(pageable);
        } else {
            page = releveNoteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /releve-notes/:id} : get the "id" releveNote.
     *
     * @param id the id of the releveNote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releveNote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/releve-notes/{id}")
    public ResponseEntity<ReleveNote> getReleveNote(@PathVariable Long id) {
        log.debug("REST request to get ReleveNote : {}", id);
        Optional<ReleveNote> releveNote = releveNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(releveNote);
    }

    /**
     * {@code DELETE  /releve-notes/:id} : delete the "id" releveNote.
     *
     * @param id the id of the releveNote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/releve-notes/{id}")
    public ResponseEntity<Void> deleteReleveNote(@PathVariable Long id) {
        log.debug("REST request to delete ReleveNote : {}", id);
        releveNoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
