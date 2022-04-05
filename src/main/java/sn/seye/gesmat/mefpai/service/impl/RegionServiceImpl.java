package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Region;
import sn.seye.gesmat.mefpai.repository.RegionRepository;
import sn.seye.gesmat.mefpai.service.RegionService;

/**
 * Service Implementation for managing {@link Region}.
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Region save(Region region) {
        log.debug("Request to save Region : {}", region);
        return regionRepository.save(region);
    }

    @Override
    public Optional<Region> partialUpdate(Region region) {
        log.debug("Request to partially update Region : {}", region);

        return regionRepository
            .findById(region.getId())
            .map(existingRegion -> {
                if (region.getNomReg() != null) {
                    existingRegion.setNomReg(region.getNomReg());
                }

                return existingRegion;
            })
            .map(regionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Region> findAll(Pageable pageable) {
        log.debug("Request to get all Regions");
        return regionRepository.findAll(pageable);
    }

    public Page<Region> findAllWithEagerRelationships(Pageable pageable) {
        return regionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Region> findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        return regionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
    }
}
