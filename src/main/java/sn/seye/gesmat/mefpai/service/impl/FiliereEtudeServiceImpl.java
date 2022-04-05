package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.FiliereEtude;
import sn.seye.gesmat.mefpai.repository.FiliereEtudeRepository;
import sn.seye.gesmat.mefpai.service.FiliereEtudeService;

/**
 * Service Implementation for managing {@link FiliereEtude}.
 */
@Service
@Transactional
public class FiliereEtudeServiceImpl implements FiliereEtudeService {

    private final Logger log = LoggerFactory.getLogger(FiliereEtudeServiceImpl.class);

    private final FiliereEtudeRepository filiereEtudeRepository;

    public FiliereEtudeServiceImpl(FiliereEtudeRepository filiereEtudeRepository) {
        this.filiereEtudeRepository = filiereEtudeRepository;
    }

    @Override
    public FiliereEtude save(FiliereEtude filiereEtude) {
        log.debug("Request to save FiliereEtude : {}", filiereEtude);
        return filiereEtudeRepository.save(filiereEtude);
    }

    @Override
    public Optional<FiliereEtude> partialUpdate(FiliereEtude filiereEtude) {
        log.debug("Request to partially update FiliereEtude : {}", filiereEtude);

        return filiereEtudeRepository
            .findById(filiereEtude.getId())
            .map(existingFiliereEtude -> {
                if (filiereEtude.getFiliere() != null) {
                    existingFiliereEtude.setFiliere(filiereEtude.getFiliere());
                }

                return existingFiliereEtude;
            })
            .map(filiereEtudeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiliereEtude> findAll(Pageable pageable) {
        log.debug("Request to get all FiliereEtudes");
        return filiereEtudeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiliereEtude> findOne(Long id) {
        log.debug("Request to get FiliereEtude : {}", id);
        return filiereEtudeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FiliereEtude : {}", id);
        filiereEtudeRepository.deleteById(id);
    }
}
