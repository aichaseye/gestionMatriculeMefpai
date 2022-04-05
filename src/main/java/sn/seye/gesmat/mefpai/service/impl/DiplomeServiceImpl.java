package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Diplome;
import sn.seye.gesmat.mefpai.repository.DiplomeRepository;
import sn.seye.gesmat.mefpai.service.DiplomeService;

/**
 * Service Implementation for managing {@link Diplome}.
 */
@Service
@Transactional
public class DiplomeServiceImpl implements DiplomeService {

    private final Logger log = LoggerFactory.getLogger(DiplomeServiceImpl.class);

    private final DiplomeRepository diplomeRepository;

    public DiplomeServiceImpl(DiplomeRepository diplomeRepository) {
        this.diplomeRepository = diplomeRepository;
    }

    @Override
    public Diplome save(Diplome diplome) {
        log.debug("Request to save Diplome : {}", diplome);
        return diplomeRepository.save(diplome);
    }

    @Override
    public Optional<Diplome> partialUpdate(Diplome diplome) {
        log.debug("Request to partially update Diplome : {}", diplome);

        return diplomeRepository
            .findById(diplome.getId())
            .map(existingDiplome -> {
                if (diplome.getNom() != null) {
                    existingDiplome.setNom(diplome.getNom());
                }
                if (diplome.getMatriculeDiplome() != null) {
                    existingDiplome.setMatriculeDiplome(diplome.getMatriculeDiplome());
                }
                if (diplome.getAnnee() != null) {
                    existingDiplome.setAnnee(diplome.getAnnee());
                }

                return existingDiplome;
            })
            .map(diplomeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Diplome> findAll(Pageable pageable) {
        log.debug("Request to get all Diplomes");
        return diplomeRepository.findAll(pageable);
    }

    public Page<Diplome> findAllWithEagerRelationships(Pageable pageable) {
        return diplomeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Diplome> findOne(Long id) {
        log.debug("Request to get Diplome : {}", id);
        return diplomeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Diplome : {}", id);
        diplomeRepository.deleteById(id);
    }
}
