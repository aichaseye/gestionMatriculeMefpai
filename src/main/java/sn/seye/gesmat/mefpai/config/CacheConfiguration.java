package sn.seye.gesmat.mefpai.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, sn.seye.gesmat.mefpai.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, sn.seye.gesmat.mefpai.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, sn.seye.gesmat.mefpai.domain.User.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Authority.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.User.class.getName() + ".authorities");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Pays.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Pays.class.getName() + ".regions");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Region.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Region.class.getName() + ".departements");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Departement.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Departement.class.getName() + ".communes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Commune.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Commune.class.getName() + ".inspections");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Commune.class.getName() + ".etablissements");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Inspection.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Inspection.class.getName() + ".personnel");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Inspection.class.getName() + ".etablissements");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Etablissement.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Etablissement.class.getName() + ".bons");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Etablissement.class.getName() + ".personnel");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Etablissement.class.getName() + ".classes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Apprenant.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Apprenant.class.getName() + ".inscriptions");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Apprenant.class.getName() + ".releveNotes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Apprenant.class.getName() + ".diplomes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Inscription.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Personnel.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Poste.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Poste.class.getName() + ".personnel");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Classe.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Classe.class.getName() + ".inscriptions");
            createCache(cm, sn.seye.gesmat.mefpai.domain.CarteScolaire.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Diplome.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.NiveauEtude.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.NiveauEtude.class.getName() + ".diplomes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.NiveauEtude.class.getName() + ".releveNotes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.SerieEtude.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.SerieEtude.class.getName() + ".diplomes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.SerieEtude.class.getName() + ".releveNotes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.FiliereEtude.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.FiliereEtude.class.getName() + ".diplomes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.FiliereEtude.class.getName() + ".releveNotes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.ReleveNote.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.ReleveNote.class.getName() + ".noteProgrammes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.NoteProgramme.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.NoteProgramme.class.getName() + ".releveNotes");
            createCache(cm, sn.seye.gesmat.mefpai.domain.DemandeMatApp.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.DemandeMatEtab.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Matiere.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Matiere.class.getName() + ".bons");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Matiere.class.getName() + ".images");
            createCache(cm, sn.seye.gesmat.mefpai.domain.Image.class.getName());
            createCache(cm, sn.seye.gesmat.mefpai.domain.Bon.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
