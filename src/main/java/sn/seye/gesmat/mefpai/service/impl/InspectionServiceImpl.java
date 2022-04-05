package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Inspection;
import sn.seye.gesmat.mefpai.repository.InspectionRepository;
import sn.seye.gesmat.mefpai.service.InspectionService;

/**
 * Service Implementation for managing {@link Inspection}.
 */
@Service
@Transactional
public class InspectionServiceImpl implements InspectionService {

    private final Logger log = LoggerFactory.getLogger(InspectionServiceImpl.class);

    private final InspectionRepository inspectionRepository;

    public InspectionServiceImpl(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public Inspection save(Inspection inspection) {
        log.debug("Request to save Inspection : {}", inspection);
        return inspectionRepository.save(inspection);
    }

    @Override
    public Optional<Inspection> partialUpdate(Inspection inspection) {
        log.debug("Request to partially update Inspection : {}", inspection);

        return inspectionRepository
            .findById(inspection.getId())
            .map(existingInspection -> {
                if (inspection.getNomInsp() != null) {
                    existingInspection.setNomInsp(inspection.getNomInsp());
                }
                if (inspection.getTypeInsp() != null) {
                    existingInspection.setTypeInsp(inspection.getTypeInsp());
                }
                if (inspection.getLatitude() != null) {
                    existingInspection.setLatitude(inspection.getLatitude());
                }
                if (inspection.getLongitude() != null) {
                    existingInspection.setLongitude(inspection.getLongitude());
                }

                return existingInspection;
            })
            .map(inspectionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inspection> findAll(Pageable pageable) {
        log.debug("Request to get all Inspections");
        return inspectionRepository.findAll(pageable);
    }

    public Page<Inspection> findAllWithEagerRelationships(Pageable pageable) {
        return inspectionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inspection> findOne(Long id) {
        log.debug("Request to get Inspection : {}", id);
        return inspectionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inspection : {}", id);
        inspectionRepository.deleteById(id);
    }
}
