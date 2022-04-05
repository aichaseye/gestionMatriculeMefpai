package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.NomPoste;

/**
 * A Poste.
 */
@Entity
@Table(name = "poste")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom_poste", nullable = false)
    private NomPoste nomPoste;

    @OneToMany(mappedBy = "poste")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "inspection", "poste" }, allowSetters = true)
    private Set<Personnel> personnel = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Poste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomPoste getNomPoste() {
        return this.nomPoste;
    }

    public Poste nomPoste(NomPoste nomPoste) {
        this.setNomPoste(nomPoste);
        return this;
    }

    public void setNomPoste(NomPoste nomPoste) {
        this.nomPoste = nomPoste;
    }

    public Set<Personnel> getPersonnel() {
        return this.personnel;
    }

    public void setPersonnel(Set<Personnel> personnel) {
        if (this.personnel != null) {
            this.personnel.forEach(i -> i.setPoste(null));
        }
        if (personnel != null) {
            personnel.forEach(i -> i.setPoste(this));
        }
        this.personnel = personnel;
    }

    public Poste personnel(Set<Personnel> personnel) {
        this.setPersonnel(personnel);
        return this;
    }

    public Poste addPersonnel(Personnel personnel) {
        this.personnel.add(personnel);
        personnel.setPoste(this);
        return this;
    }

    public Poste removePersonnel(Personnel personnel) {
        this.personnel.remove(personnel);
        personnel.setPoste(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poste)) {
            return false;
        }
        return id != null && id.equals(((Poste) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poste{" +
            "id=" + getId() +
            ", nomPoste='" + getNomPoste() + "'" +
            "}";
    }
}
