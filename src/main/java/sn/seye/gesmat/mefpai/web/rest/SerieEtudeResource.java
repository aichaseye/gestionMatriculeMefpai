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
import sn.seye.gesmat.mefpai.domain.SerieEtude;
import sn.seye.gesmat.mefpai.repository.SerieEtudeRepository;
import sn.seye.gesmat.mefpai.service.SerieEtudeService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.SerieEtude}.
 */
@RestController
@RequestMapping("/api")
public class SerieEtudeResource {

    private final Logger log = LoggerFactory.getLogger(SerieEtudeResource.class);

    private static final String ENTITY_NAME = "serieEtude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SerieEtudeService serieEtudeService;

    private final SerieEtudeRepository serieEtudeRepository;

    public SerieEtudeResource(SerieEtudeService serieEtudeService, SerieEtudeRepository serieEtudeRepository) {
        this.serieEtudeService = serieEtudeService;
        this.serieEtudeRepository = serieEtudeRepository;
    }

    /**
     * {@code POST  /serie-etudes} : Create a new serieEtude.
     *
     * @param serieEtude the serieEtude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serieEtude, or with status {@code 400 (Bad Request)} if the serieEtude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/serie-etudes")
    public ResponseEntity<SerieEtude> createSerieEtude(@Valid @RequestBody SerieEtude serieEtude) throws URISyntaxException {
        log.debug("REST request to save SerieEtude : {}", serieEtude);
        if (serieEtude.getId() != null) {
            throw new BadRequestAlertException("A new serieEtude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SerieEtude result = serieEtudeService.save(serieEtude);
        return ResponseEntity
            .created(new URI("/api/serie-etudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /serie-etudes/:id} : Updates an existing serieEtude.
     *
     * @param id the id of the serieEtude to save.
     * @param serieEtude the serieEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serieEtude,
     * or with status {@code 400 (Bad Request)} if the serieEtude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serieEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/serie-etudes/{id}")
    public ResponseEntity<SerieEtude> updateSerieEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SerieEtude serieEtude
    ) throws URISyntaxException {
        log.debug("REST request to update SerieEtude : {}, {}", id, serieEtude);
        if (serieEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serieEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serieEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SerieEtude result = serieEtudeService.save(serieEtude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serieEtude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /serie-etudes/:id} : Partial updates given fields of an existing serieEtude, field will ignore if it is null
     *
     * @param id the id of the serieEtude to save.
     * @param serieEtude the serieEtude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serieEtude,
     * or with status {@code 400 (Bad Request)} if the serieEtude is not valid,
     * or with status {@code 404 (Not Found)} if the serieEtude is not found,
     * or with status {@code 500 (Internal Server Error)} if the serieEtude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/serie-etudes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SerieEtude> partialUpdateSerieEtude(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SerieEtude serieEtude
    ) throws URISyntaxException {
        log.debug("REST request to partial update SerieEtude partially : {}, {}", id, serieEtude);
        if (serieEtude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serieEtude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serieEtudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SerieEtude> result = serieEtudeService.partialUpdate(serieEtude);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serieEtude.getId().toString())
        );
    }

    /**
     * {@code GET  /serie-etudes} : get all the serieEtudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serieEtudes in body.
     */
    @GetMapping("/serie-etudes")
    public ResponseEntity<List<SerieEtude>> getAllSerieEtudes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SerieEtudes");
        Page<SerieEtude> page = serieEtudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /serie-etudes/:id} : get the "id" serieEtude.
     *
     * @param id the id of the serieEtude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serieEtude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/serie-etudes/{id}")
    public ResponseEntity<SerieEtude> getSerieEtude(@PathVariable Long id) {
        log.debug("REST request to get SerieEtude : {}", id);
        Optional<SerieEtude> serieEtude = serieEtudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serieEtude);
    }

    /**
     * {@code DELETE  /serie-etudes/:id} : delete the "id" serieEtude.
     *
     * @param id the id of the serieEtude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/serie-etudes/{id}")
    public ResponseEntity<Void> deleteSerieEtude(@PathVariable Long id) {
        log.debug("REST request to delete SerieEtude : {}", id);
        serieEtudeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
