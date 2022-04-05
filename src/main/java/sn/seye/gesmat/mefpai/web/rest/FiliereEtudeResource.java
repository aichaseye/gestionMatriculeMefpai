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
import sn.seye.gesmat.mefpai.domain.FiliereEtude;
import sn.seye.gesmat.mefpai.repository.FiliereEtudeRepository;
import sn.seye.gesmat.mefpai.service.FiliereEtudeService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.FiliereEtude}.
 */
@RestController
@RequestMapping("/api")
public class FiliereEtudeResource {

    private final Logger log = LoggerFactory.getLogger(FiliereEtudeResource.class);

    private static final String ENTITY_NAME = "filiereEtude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiliereEtudeService filiereEtudeService;

    private final FiliereEtudeRepository filiereEtudeRepository;

    public FiliereEtudeResource(FiliereEtudeService filiereEtudeService, FiliereEtudeRepository filiereEtudeRepository) {
        this.filiereEtudeService = filiereEtudeService;
        this.filiereEtudeRepository = filiereEtudeRepository;
    }

    /**
     * {@code POST  /filiere-etudes} : Create a new filiereEtude.
     *
     * @param filiereEtude the filiereEtude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filiereEtude, or with status {@code 400 (Bad Request)} if the filiereEtude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filiere-etudes")
    public ResponseEntity<FiliereEtude> createFiliereEtude(@Valid @RequestBody FiliereEtude filiereEtude) throws URISyntaxException {
        log.debug("REST request to save FiliereEtude : {}", filiereEtude);
        if (filiereEtude.getId() != null) {
            throw new BadRequestAlertException("A new filiereEtude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiliereEtude result = filiereEtudeService.save(filiereEtude);
        return ResponseEntity
            .created(new URI("/api/filiere-etudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filiere-etudes/:id} : Updates an existing filiereEtude.
     *
     * @param id the id of the filiereEtude to save.
     * @param filiereEtude the filiereEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereEtude,
     * or with status {@code 400 (Bad Request)} if the filiereEtude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filiereEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filiere-etudes/{id}")
    public ResponseEntity<FiliereEtude> updateFiliereEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiliereEtude filiereEtude
    ) throws URISyntaxException {
        log.debug("REST request to update FiliereEtude : {}, {}", id, filiereEtude);
        if (filiereEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiliereEtude result = filiereEtudeService.save(filiereEtude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereEtude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filiere-etudes/:id} : Partial updates given fields of an existing filiereEtude, field will ignore if it is null
     *
     * @param id the id of the filiereEtude to save.
     * @param filiereEtude the filiereEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereEtude,
     * or with status {@code 400 (Bad Request)} if the filiereEtude is not valid,
     * or with status {@code 404 (Not Found)} if the filiereEtude is not found,
     * or with status {@code 500 (Internal Server Error)} if the filiereEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filiere-etudes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiliereEtude> partialUpdateFiliereEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiliereEtude filiereEtude
    ) throws URISyntaxException {
        log.debug("REST request to partial update FiliereEtude partially : {}, {}", id, filiereEtude);
        if (filiereEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiliereEtude> result = filiereEtudeService.partialUpdate(filiereEtude);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereEtude.getId().toString())
        );
    }

    /**
     * {@code GET  /filiere-etudes} : get all the filiereEtudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filiereEtudes in body.
     */
    @GetMapping("/filiere-etudes")
    public ResponseEntity<List<FiliereEtude>> getAllFiliereEtudes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FiliereEtudes");
        Page<FiliereEtude> page = filiereEtudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filiere-etudes/:id} : get the "id" filiereEtude.
     *
     * @param id the id of the filiereEtude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filiereEtude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filiere-etudes/{id}")
    public ResponseEntity<FiliereEtude> getFiliereEtude(@PathVariable Long id) {
        log.debug("REST request to get FiliereEtude : {}", id);
        Optional<FiliereEtude> filiereEtude = filiereEtudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filiereEtude);
    }

    /**
     * {@code DELETE  /filiere-etudes/:id} : delete the "id" filiereEtude.
     *
     * @param id the id of the filiereEtude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filiere-etudes/{id}")
    public ResponseEntity<Void> deleteFiliereEtude(@PathVariable Long id) {
        log.debug("REST request to delete FiliereEtude : {}", id);
        filiereEtudeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
