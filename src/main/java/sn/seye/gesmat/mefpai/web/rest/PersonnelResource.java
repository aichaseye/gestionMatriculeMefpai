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
import sn.seye.gesmat.mefpai.domain.Personnel;
import sn.seye.gesmat.mefpai.repository.PersonnelRepository;
import sn.seye.gesmat.mefpai.service.PersonnelService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Personnel}.
 */
@RestController
@RequestMapping("/api")
public class PersonnelResource {

    private final Logger log = LoggerFactory.getLogger(PersonnelResource.class);

    private static final String ENTITY_NAME = "personnel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonnelService personnelService;

    private final PersonnelRepository personnelRepository;

    public PersonnelResource(PersonnelService personnelService, PersonnelRepository personnelRepository) {
        this.personnelService = personnelService;
        this.personnelRepository = personnelRepository;
    }

    /**
     * {@code POST  /personnel} : Create a new personnel.
     *
     * @param personnel the personnel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personnel, or with status {@code 400 (Bad Request)} if the personnel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personnel")
    public ResponseEntity<Personnel> createPersonnel(@Valid @RequestBody Personnel personnel) throws URISyntaxException {
        log.debug("REST request to save Personnel : {}", personnel);
        if (personnel.getId() != null) {
            throw new BadRequestAlertException("A new personnel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Personnel result = personnelService.save(personnel);
        return ResponseEntity
            .created(new URI("/api/personnel/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personnel/:id} : Updates an existing personnel.
     *
     * @param id the id of the personnel to save.
     * @param personnel the personnel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnel,
     * or with status {@code 400 (Bad Request)} if the personnel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personnel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personnel/{id}")
    public ResponseEntity<Personnel> updatePersonnel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Personnel personnel
    ) throws URISyntaxException {
        log.debug("REST request to update Personnel : {}, {}", id, personnel);
        if (personnel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personnel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personnelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Personnel result = personnelService.save(personnel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personnel/:id} : Partial updates given fields of an existing personnel, field will ignore if it is null
     *
     * @param id the id of the personnel to save.
     * @param personnel the personnel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnel,
     * or with status {@code 400 (Bad Request)} if the personnel is not valid,
     * or with status {@code 404 (Not Found)} if the personnel is not found,
     * or with status {@code 500 (Internal Server Error)} if the personnel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personnel/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Personnel> partialUpdatePersonnel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Personnel personnel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personnel partially : {}, {}", id, personnel);
        if (personnel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personnel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personnelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Personnel> result = personnelService.partialUpdate(personnel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnel.getId().toString())
        );
    }

    /**
     * {@code GET  /personnel} : get all the personnel.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnel in body.
     */
    @GetMapping("/personnel")
    public ResponseEntity<List<Personnel>> getAllPersonnel(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Personnel");
        Page<Personnel> page;
        if (eagerload) {
            page = personnelService.findAllWithEagerRelationships(pageable);
        } else {
            page = personnelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personnel/:id} : get the "id" personnel.
     *
     * @param id the id of the personnel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personnel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personnel/{id}")
    public ResponseEntity<Personnel> getPersonnel(@PathVariable Long id) {
        log.debug("REST request to get Personnel : {}", id);
        Optional<Personnel> personnel = personnelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personnel);
    }

    /**
     * {@code DELETE  /personnel/:id} : delete the "id" personnel.
     *
     * @param id the id of the personnel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personnel/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        log.debug("REST request to delete Personnel : {}", id);
        personnelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
