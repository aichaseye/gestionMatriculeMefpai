package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Bon;
import sn.seye.gesmat.mefpai.repository.BonRepository;
import sn.seye.gesmat.mefpai.service.BonService;

/**
 * Service Implementation for managing {@link Bon}.
 */
@Service
@Transactional
public class BonServiceImpl implements BonService {

    private final Logger log = LoggerFactory.getLogger(BonServiceImpl.class);

    private final BonRepository bonRepository;

    public BonServiceImpl(BonRepository bonRepository) {
        this.bonRepository = bonRepository;
    }

    @Override
    public Bon save(Bon bon) {
        log.debug("Request to save Bon : {}", bon);
        return bonRepository.save(bon);
    }

    @Override
    public Optional<Bon> partialUpdate(Bon bon) {
        log.debug("Request to partially update Bon : {}", bon);

        return bonRepository
            .findById(bon.getId())
            .map(existingBon -> {
                if (bon.getQuantite() != null) {
                    existingBon.setQuantite(bon.getQuantite());
                }
                if (bon.getDate() != null) {
                    existingBon.setDate(bon.getDate());
                }
                if (bon.getDescription() != null) {
                    existingBon.setDescription(bon.getDescription());
                }

                return existingBon;
            })
            .map(bonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bon> findAll(Pageable pageable) {
        log.debug("Request to get all Bons");
        return bonRepository.findAll(pageable);
    }

    public Page<Bon> findAllWithEagerRelationships(Pageable pageable) {
        return bonRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bon> findOne(Long id) {
        log.debug("Request to get Bon : {}", id);
        return bonRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bon : {}", id);
        bonRepository.deleteById(id);
    }
}
