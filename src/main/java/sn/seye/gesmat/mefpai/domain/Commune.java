package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commune.
 */
@Entity
@Table(name = "commune")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_com", nullable = false)
    private String nomCom;

    @OneToMany(mappedBy = "commune")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personnel", "etablissements", "commune" }, allowSetters = true)
    private Set<Inspection> inspections = new HashSet<>();

    @OneToMany(mappedBy = "commune")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bons", "personnel", "classes", "commune", "inspection" }, allowSetters = true)
    private Set<Etablissement> etablissements = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "communes", "region" }, allowSetters = true)
    private Departement departement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commune id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCom() {
        return this.nomCom;
    }

    public Commune nomCom(String nomCom) {
        this.setNomCom(nomCom);
        return this;
    }

    public void setNomCom(String nomCom) {
        this.nomCom = nomCom;
    }

    public Set<Inspection> getInspections() {
        return this.inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        if (this.inspections != null) {
            this.inspections.forEach(i -> i.setCommune(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setCommune(this));
        }
        this.inspections = inspections;
    }

    public Commune inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Commune addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setCommune(this);
        return this;
    }

    public Commune removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setCommune(null);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.setCommune(null));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.setCommune(this));
        }
        this.etablissements = etablissements;
    }

    public Commune etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Commune addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.setCommune(this);
        return this;
    }

    public Commune removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.setCommune(null);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Commune departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", nomCom='" + getNomCom() + "'" +
            "}";
    }
}
