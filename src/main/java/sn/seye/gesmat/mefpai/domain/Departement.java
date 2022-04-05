package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.NomDep;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom_dep", nullable = false)
    private NomDep nomDep;

    @OneToMany(mappedBy = "departement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inspections", "etablissements", "departement" }, allowSetters = true)
    private Set<Commune> communes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "departements", "pays" }, allowSetters = true)
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomDep getNomDep() {
        return this.nomDep;
    }

    public Departement nomDep(NomDep nomDep) {
        this.setNomDep(nomDep);
        return this;
    }

    public void setNomDep(NomDep nomDep) {
        this.nomDep = nomDep;
    }

    public Set<Commune> getCommunes() {
        return this.communes;
    }

    public void setCommunes(Set<Commune> communes) {
        if (this.communes != null) {
            this.communes.forEach(i -> i.setDepartement(null));
        }
        if (communes != null) {
            communes.forEach(i -> i.setDepartement(this));
        }
        this.communes = communes;
    }

    public Departement communes(Set<Commune> communes) {
        this.setCommunes(communes);
        return this;
    }

    public Departement addCommune(Commune commune) {
        this.communes.add(commune);
        commune.setDepartement(this);
        return this;
    }

    public Departement removeCommune(Commune commune) {
        this.communes.remove(commune);
        commune.setDepartement(null);
        return this;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Departement region(Region region) {
        this.setRegion(region);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nomDep='" + getNomDep() + "'" +
            "}";
    }
}
