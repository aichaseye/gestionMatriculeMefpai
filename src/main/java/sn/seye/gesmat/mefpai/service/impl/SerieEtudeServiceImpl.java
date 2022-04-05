package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.SerieEtude;
import sn.seye.gesmat.mefpai.repository.SerieEtudeRepository;
import sn.seye.gesmat.mefpai.service.SerieEtudeService;

/**
 * Service Implementation for managing {@link SerieEtude}.
 */
@Service
@Transactional
public class SerieEtudeServiceImpl implements SerieEtudeService {

    private final Logger log = LoggerFactory.getLogger(SerieEtudeServiceImpl.class);

    private final SerieEtudeRepository serieEtudeRepository;

    public SerieEtudeServiceImpl(SerieEtudeRepository serieEtudeRepository) {
        this.serieEtudeRepository = serieEtudeRepository;
    }

    @Override
    public SerieEtude save(SerieEtude serieEtude) {
        log.debug("Request to save SerieEtude : {}", serieEtude);
        return serieEtudeRepository.save(serieEtude);
    }

    @Override
    public Optional<SerieEtude> partialUpdate(SerieEtude serieEtude) {
        log.debug("Request to partially update SerieEtude : {}", serieEtude);

        return serieEtudeRepository
            .findById(serieEtude.getId())
            .map(existingSerieEtude -> {
                if (serieEtude.getSerie() != null) {
                    existingSerieEtude.setSerie(serieEtude.getSerie());
                }

                return existingSerieEtude;
            })
            .map(serieEtudeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SerieEtude> findAll(Pageable pageable) {
        log.debug("Request to get all SerieEtudes");
        return serieEtudeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SerieEtude> findOne(Long id) {
        log.debug("Request to get SerieEtude : {}", id);
        return serieEtudeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SerieEtude : {}", id);
        serieEtudeRepository.deleteById(id);
    }
}
