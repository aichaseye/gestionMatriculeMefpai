package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.NoteProgramme;
import sn.seye.gesmat.mefpai.repository.NoteProgrammeRepository;
import sn.seye.gesmat.mefpai.service.NoteProgrammeService;

/**
 * Service Implementation for managing {@link NoteProgramme}.
 */
@Service
@Transactional
public class NoteProgrammeServiceImpl implements NoteProgrammeService {

    private final Logger log = LoggerFactory.getLogger(NoteProgrammeServiceImpl.class);

    private final NoteProgrammeRepository noteProgrammeRepository;

    public NoteProgrammeServiceImpl(NoteProgrammeRepository noteProgrammeRepository) {
        this.noteProgrammeRepository = noteProgrammeRepository;
    }

    @Override
    public NoteProgramme save(NoteProgramme noteProgramme) {
        log.debug("Request to save NoteProgramme : {}", noteProgramme);
        return noteProgrammeRepository.save(noteProgramme);
    }

    @Override
    public Optional<NoteProgramme> partialUpdate(NoteProgramme noteProgramme) {
        log.debug("Request to partially update NoteProgramme : {}", noteProgramme);

        return noteProgrammeRepository
            .findById(noteProgramme.getId())
            .map(existingNoteProgramme -> {
                if (noteProgramme.getNomProg() != null) {
                    existingNoteProgramme.setNomProg(noteProgramme.getNomProg());
                }
                if (noteProgramme.getCoef() != null) {
                    existingNoteProgramme.setCoef(noteProgramme.getCoef());
                }
                if (noteProgramme.getNote() != null) {
                    existingNoteProgramme.setNote(noteProgramme.getNote());
                }

                return existingNoteProgramme;
            })
            .map(noteProgrammeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteProgramme> findAll(Pageable pageable) {
        log.debug("Request to get all NoteProgrammes");
        return noteProgrammeRepository.findAll(pageable);
    }

    public Page<NoteProgramme> findAllWithEagerRelationships(Pageable pageable) {
        return noteProgrammeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NoteProgramme> findOne(Long id) {
        log.debug("Request to get NoteProgramme : {}", id);
        return noteProgrammeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NoteProgramme : {}", id);
        noteProgrammeRepository.deleteById(id);
    }
}
