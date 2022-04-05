package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Matiere;
import sn.seye.gesmat.mefpai.repository.MatiereRepository;
import sn.seye.gesmat.mefpai.service.MatiereService;

/**
 * Service Implementation for managing {@link Matiere}.
 */
@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereServiceImpl.class);

    private final MatiereRepository matiereRepository;

    public MatiereServiceImpl(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    @Override
    public Matiere save(Matiere matiere) {
        log.debug("Request to save Matiere : {}", matiere);
        return matiereRepository.save(matiere);
    }

    @Override
    public Optional<Matiere> partialUpdate(Matiere matiere) {
        log.debug("Request to partially update Matiere : {}", matiere);

        return matiereRepository
            .findById(matiere.getId())
            .map(existingMatiere -> {
                if (matiere.getNomMatiere() != null) {
                    existingMatiere.setNomMatiere(matiere.getNomMatiere());
                }
                if (matiere.getReference() != null) {
                    existingMatiere.setReference(matiere.getReference());
                }
                if (matiere.getTypeMatiere() != null) {
                    existingMatiere.setTypeMatiere(matiere.getTypeMatiere());
                }
                if (matiere.getMatriculeMatiere() != null) {
                    existingMatiere.setMatriculeMatiere(matiere.getMatriculeMatiere());
                }

                return existingMatiere;
            })
            .map(matiereRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Matiere> findAll(Pageable pageable) {
        log.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Matiere> findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        return matiereRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
