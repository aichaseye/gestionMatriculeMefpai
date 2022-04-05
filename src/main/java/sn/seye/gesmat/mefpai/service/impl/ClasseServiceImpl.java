package sn.seye.gesmat.mefpai.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.seye.gesmat.mefpai.domain.Classe;
import sn.seye.gesmat.mefpai.repository.ClasseRepository;
import sn.seye.gesmat.mefpai.service.ClasseService;

/**
 * Service Implementation for managing {@link Classe}.
 */
@Service
@Transactional
public class ClasseServiceImpl implements ClasseService {

    private final Logger log = LoggerFactory.getLogger(ClasseServiceImpl.class);

    private final ClasseRepository classeRepository;

    public ClasseServiceImpl(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    @Override
    public Classe save(Classe classe) {
        log.debug("Request to save Classe : {}", classe);
        return classeRepository.save(classe);
    }

    @Override
    public Optional<Classe> partialUpdate(Classe classe) {
        log.debug("Request to partially update Classe : {}", classe);

        return classeRepository
            .findById(classe.getId())
            .map(existingClasse -> {
                if (classe.getNomClasse() != null) {
                    existingClasse.setNomClasse(classe.getNomClasse());
                }

                return existingClasse;
            })
            .map(classeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Classe> findAll(Pageable pageable) {
        log.debug("Request to get all Classes");
        return classeRepository.findAll(pageable);
    }

    public Page<Classe> findAllWithEagerRelationships(Pageable pageable) {
        return classeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Classe> findOne(Long id) {
        log.debug("Request to get Classe : {}", id);
        return classeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Classe : {}", id);
        classeRepository.deleteById(id);
    }
}
