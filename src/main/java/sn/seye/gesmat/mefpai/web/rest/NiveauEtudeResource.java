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
import sn.seye.gesmat.mefpai.domain.NiveauEtude;
import sn.seye.gesmat.mefpai.repository.NiveauEtudeRepository;
import sn.seye.gesmat.mefpai.service.NiveauEtudeService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.NiveauEtude}.
 */
@RestController
@RequestMapping("/api")
public class NiveauEtudeResource {

    private final Logger log = LoggerFactory.getLogger(NiveauEtudeResource.class);

    private static final String ENTITY_NAME = "niveauEtude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NiveauEtudeService niveauEtudeService;

    private final NiveauEtudeRepository niveauEtudeRepository;

    public NiveauEtudeResource(NiveauEtudeService niveauEtudeService, NiveauEtudeRepository niveauEtudeRepository) {
        this.niveauEtudeService = niveauEtudeService;
        this.niveauEtudeRepository = niveauEtudeRepository;
    }

    /**
     * {@code POST  /niveau-etudes} : Create a new niveauEtude.
     *
     * @param niveauEtude the niveauEtude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new niveauEtude, or with status {@code 400 (Bad Request)} if the niveauEtude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/niveau-etudes")
    public ResponseEntity<NiveauEtude> createNiveauEtude(@Valid @RequestBody NiveauEtude niveauEtude) throws URISyntaxException {
        log.debug("REST request to save NiveauEtude : {}", niveauEtude);
        if (niveauEtude.getId() != null) {
            throw new BadRequestAlertException("A new niveauEtude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NiveauEtude result = niveauEtudeService.save(niveauEtude);
        return ResponseEntity
            .created(new URI("/api/niveau-etudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /niveau-etudes/:id} : Updates an existing niveauEtude.
     *
     * @param id the id of the niveauEtude to save.
     * @param niveauEtude the niveauEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated niveauEtude,
     * or with status {@code 400 (Bad Request)} if the niveauEtude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the niveauEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/niveau-etudes/{id}")
    public ResponseEntity<NiveauEtude> updateNiveauEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NiveauEtude niveauEtude
    ) throws URISyntaxException {
        log.debug("REST request to update NiveauEtude : {}, {}", id, niveauEtude);
        if (niveauEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, niveauEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!niveauEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NiveauEtude result = niveauEtudeService.save(niveauEtude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, niveauEtude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /niveau-etudes/:id} : Partial updates given fields of an existing niveauEtude, field will ignore if it is null
     *
     * @param id the id of the niveauEtude to save.
     * @param niveauEtude the niveauEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated niveauEtude,
     * or with status {@code 400 (Bad Request)} if the niveauEtude is not valid,
     * or with status {@code 404 (Not Found)} if the niveauEtude is not found,
     * or with status {@code 500 (Internal Server Error)} if the niveauEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/niveau-etudes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NiveauEtude> partialUpdateNiveauEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NiveauEtude niveauEtude
    ) throws URISyntaxException {
        log.debug("REST request to partial update NiveauEtude partially : {}, {}", id, niveauEtude);
        if (niveauEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, niveauEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!niveauEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NiveauEtude> result = niveauEtudeService.partialUpdate(niveauEtude);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, niveauEtude.getId().toString())
        );
    }

    /**
     * {@code GET  /niveau-etudes} : get all the niveauEtudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of niveauEtudes in body.
     */
    @GetMapping("/niveau-etudes")
    public ResponseEntity<List<NiveauEtude>> getAllNiveauEtudes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NiveauEtudes");
        Page<NiveauEtude> page = niveauEtudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /niveau-etudes/:id} : get the "id" niveauEtude.
     *
     * @param id the id of the niveauEtude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the niveauEtude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/niveau-etudes/{id}")
    public ResponseEntity<NiveauEtude> getNiveauEtude(@PathVariable Long id) {
        log.debug("REST request to get NiveauEtude : {}", id);
        Optional<NiveauEtude> niveauEtude = niveauEtudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(niveauEtude);
    }

    /**
     * {@code DELETE  /niveau-etudes/:id} : delete the "id" niveauEtude.
     *
     * @param id the id of the niveauEtude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/niveau-etudes/{id}")
    public ResponseEntity<Void> deleteNiveauEtude(@PathVariable Long id) {
        log.debug("REST request to delete NiveauEtude : {}", id);
        niveauEtudeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
