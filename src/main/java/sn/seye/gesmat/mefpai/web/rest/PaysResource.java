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
import sn.seye.gesmat.mefpai.domain.Pays;
import sn.seye.gesmat.mefpai.repository.PaysRepository;
import sn.seye.gesmat.mefpai.service.PaysService;
import sn.seye.gesmat.mefpai.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.seye.gesmat.mefpai.domain.Pays}.
 */
@RestController
@RequestMapping("/api")
public class PaysResource {

    private final Logger log = LoggerFactory.getLogger(PaysResource.class);

    private static final String ENTITY_NAME = "pays";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaysService paysService;

    private final PaysRepository paysRepository;

    public PaysResource(PaysService paysService, PaysRepository paysRepository) {
        this.paysService = paysService;
        this.paysRepository = paysRepository;
    }

    /**
     * {@code POST  /pays} : Create a new pays.
     *
     * @param pays the pays to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pays, or with status {@code 400 (Bad Request)} if the pays has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pays")
    public ResponseEntity<Pays> createPays(@Valid @RequestBody Pays pays) throws URISyntaxException {
        log.debug("REST request to save Pays : {}", pays);
        if (pays.getId() != null) {
            throw new BadRequestAlertException("A new pays cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pays result = paysService.save(pays);
        return ResponseEntity
            .created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pays/:id} : Updates an existing pays.
     *
     * @param id the id of the pays to save.
     * @param pays the pays to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pays,
     * or with status {@code 400 (Bad Request)} if the pays is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pays couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pays/{id}")
    public ResponseEntity<Pays> updatePays(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Pays pays)
        throws URISyntaxException {
        log.debug("REST request to update Pays : {}, {}", id, pays);
        if (pays.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pays.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pays result = paysService.save(pays);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pays.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pays/:id} : Partial updates given fields of an existing pays, field will ignore if it is null
     *
     * @param id the id of the pays to save.
     * @param pays the pays to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pays,
     * or with status {@code 400 (Bad Request)} if the pays is not valid,
     * or with status {@code 404 (Not Found)} if the pays is not found,
     * or with status {@code 500 (Internal Server Error)} if the pays couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pays> partialUpdatePays(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pays pays
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pays partially : {}, {}", id, pays);
        if (pays.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pays.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pays> result = paysService.partialUpdate(pays);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pays.getId().toString())
        );
    }

    /**
     * {@code GET  /pays} : get all the pays.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pays in body.
     */
    @GetMapping("/pays")
    public ResponseEntity<List<Pays>> getAllPays(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pays");
        Page<Pays> page = paysService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pays/:id} : get the "id" pays.
     *
     * @param id the id of the pays to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pays, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pays/{id}")
    public ResponseEntity<Pays> getPays(@PathVariable Long id) {
        log.debug("REST request to get Pays : {}", id);
        Optional<Pays> pays = paysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pays);
    }

    /**
     * {@code DELETE  /pays/:id} : delete the "id" pays.
     *
     * @param id the id of the pays to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Void> deletePays(@PathVariable Long id) {
        log.debug("REST request to delete Pays : {}", id);
        paysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
