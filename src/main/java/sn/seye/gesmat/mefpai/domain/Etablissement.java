package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.StatutEtab;
import sn.seye.gesmat.mefpai.domain.enumeration.TypeEtab;

/**
 * Task entity.\n@author The JHipster team.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_etab", nullable = false)
    private String nomEtab;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_etab", nullable = false)
    private TypeEtab typeEtab;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutEtab statut;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "matricule_etab")
    private String matriculeEtab;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "matiere" }, allowSetters = true)
    private Set<Bon> bons = new HashSet<>();

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "inspection", "poste" }, allowSetters = true)
    private Set<Personnel> personnel = new HashSet<>();

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inscriptions", "etablissement" }, allowSetters = true)
    private Set<Classe> classes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "inspections", "etablissements", "departement" }, allowSetters = true)
    private Commune commune;

    @ManyToOne
    @JsonIgnoreProperties(value = { "personnel", "etablissements", "commune" }, allowSetters = true)
    private Inspection inspection;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtab() {
        return this.nomEtab;
    }

    public Etablissement nomEtab(String nomEtab) {
        this.setNomEtab(nomEtab);
        return this;
    }

    public void setNomEtab(String nomEtab) {
        this.nomEtab = nomEtab;
    }

    public TypeEtab getTypeEtab() {
        return this.typeEtab;
    }

    public Etablissement typeEtab(TypeEtab typeEtab) {
        this.setTypeEtab(typeEtab);
        return this;
    }

    public void setTypeEtab(TypeEtab typeEtab) {
        this.typeEtab = typeEtab;
    }

    public StatutEtab getStatut() {
        return this.statut;
    }

    public Etablissement statut(StatutEtab statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutEtab statut) {
        this.statut = statut;
    }

    public Long getLatitude() {
        return this.latitude;
    }

    public Etablissement latitude(Long latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return this.longitude;
    }

    public Etablissement longitude(Long longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getMatriculeEtab() {
        return this.matriculeEtab;
    }

    public Etablissement matriculeEtab(String matriculeEtab) {
        this.setMatriculeEtab(matriculeEtab);
        return this;
    }

    public void setMatriculeEtab(String matriculeEtab) {
        this.matriculeEtab = matriculeEtab;
    }

    public Set<Bon> getBons() {
        return this.bons;
    }

    public void setBons(Set<Bon> bons) {
        if (this.bons != null) {
            this.bons.forEach(i -> i.setEtablissement(null));
        }
        if (bons != null) {
            bons.forEach(i -> i.setEtablissement(this));
        }
        this.bons = bons;
    }

    public Etablissement bons(Set<Bon> bons) {
        this.setBons(bons);
        return this;
    }

    public Etablissement addBon(Bon bon) {
        this.bons.add(bon);
        bon.setEtablissement(this);
        return this;
    }

    public Etablissement removeBon(Bon bon) {
        this.bons.remove(bon);
        bon.setEtablissement(null);
        return this;
    }

    public Set<Personnel> getPersonnel() {
        return this.personnel;
    }

    public void setPersonnel(Set<Personnel> personnel) {
        if (this.personnel != null) {
            this.personnel.forEach(i -> i.setEtablissement(null));
        }
        if (personnel != null) {
            personnel.forEach(i -> i.setEtablissement(this));
        }
        this.personnel = personnel;
    }

    public Etablissement personnel(Set<Personnel> personnel) {
        this.setPersonnel(personnel);
        return this;
    }

    public Etablissement addPersonnel(Personnel personnel) {
        this.personnel.add(personnel);
        personnel.setEtablissement(this);
        return this;
    }

    public Etablissement removePersonnel(Personnel personnel) {
        this.personnel.remove(personnel);
        personnel.setEtablissement(null);
        return this;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setEtablissement(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setEtablissement(this));
        }
        this.classes = classes;
    }

    public Etablissement classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Etablissement addClasse(Classe classe) {
        this.classes.add(classe);
        classe.setEtablissement(this);
        return this;
    }

    public Etablissement removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.setEtablissement(null);
        return this;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Etablissement commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    public Inspection getInspection() {
        return this.inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Etablissement inspection(Inspection inspection) {
        this.setInspection(inspection);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtab='" + getNomEtab() + "'" +
            ", typeEtab='" + getTypeEtab() + "'" +
            ", statut='" + getStatut() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", matriculeEtab='" + getMatriculeEtab() + "'" +
            "}";
    }
}
