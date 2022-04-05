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
import sn.seye.gesmat.mefpai.domain.Poste;
import sn.seye.gesmat.mefpai.repository.PosteRepository;
import sn.seye.gesmat.mefpai.service.PosteService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Poste}.
 */
@RestController
@RequestMapping("/api")
public class PosteResource {

    private final Logger log = LoggerFactory.getLogger(PosteResource.class);

    private static final String ENTITY_NAME = "poste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PosteService posteService;

    private final PosteRepository posteRepository;

    public PosteResource(PosteService posteService, PosteRepository posteRepository) {
        this.posteService = posteService;
        this.posteRepository = posteRepository;
    }

    /**
     * {@code POST  /postes} : Create a new poste.
     *
     * @param poste the poste to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poste, or with status {@code 400 (Bad Request)} if the poste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/postes")
    public ResponseEntity<Poste> createPoste(@Valid @RequestBody Poste poste) throws URISyntaxException {
        log.debug("REST request to save Poste : {}", poste);
        if (poste.getId() != null) {
            throw new BadRequestAlertException("A new poste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Poste result = posteService.save(poste);
        return ResponseEntity
            .created(new URI("/api/postes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /postes/:id} : Updates an existing poste.
     *
     * @param id the id of the poste to save.
     * @param poste the poste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poste,
     * or with status {@code 400 (Bad Request)} if the poste is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/postes/{id}")
    public ResponseEntity<Poste> updatePoste(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Poste poste)
        throws URISyntaxException {
        log.debug("REST request to update Poste : {}, {}", id, poste);
        if (poste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Poste result = posteService.save(poste);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poste.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /postes/:id} : Partial updates given fields of an existing poste, field will ignore if it is null
     *
     * @param id the id of the poste to save.
     * @param poste the poste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poste,
     * or with status {@code 400 (Bad Request)} if the poste is not valid,
     * or with status {@code 404 (Not Found)} if the poste is not found,
     * or with status {@code 500 (Internal Server Error)} if the poste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/postes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Poste> partialUpdatePoste(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Poste poste
    ) throws URISyntaxException {
        log.debug("REST request to partial update Poste partially : {}, {}", id, poste);
        if (poste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Poste> result = posteService.partialUpdate(poste);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poste.getId().toString())
        );
    }

    /**
     * {@code GET  /postes} : get all the postes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postes in body.
     */
    @GetMapping("/postes")
    public ResponseEntity<List<Poste>> getAllPostes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Postes");
        Page<Poste> page = posteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /postes/:id} : get the "id" poste.
     *
     * @param id the id of the poste to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poste, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/postes/{id}")
    public ResponseEntity<Poste> getPoste(@PathVariable Long id) {
        log.debug("REST request to get Poste : {}", id);
        Optional<Poste> poste = posteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(poste);
    }

    /**
     * {@code DELETE  /postes/:id} : delete the "id" poste.
     *
     * @param id the id of the poste to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/postes/{id}")
    public ResponseEntity<Void> deletePoste(@PathVariable Long id) {
        log.debug("REST request to delete Poste : {}", id);
        posteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
