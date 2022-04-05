package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.Filiere;

/**
 * A FiliereEtude.
 */
@Entity
@Table(name = "filiere_etude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FiliereEtude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "filiere", nullable = false)
    private Filiere filiere;

    @OneToMany(mappedBy = "filiereEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude" }, allowSetters = true)
    private Set<Diplome> diplomes = new HashSet<>();

    @OneToMany(mappedBy = "filiereEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude", "noteProgrammes" }, allowSetters = true)
    private Set<ReleveNote> releveNotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FiliereEtude id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public FiliereEtude filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Set<Diplome> getDiplomes() {
        return this.diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        if (this.diplomes != null) {
            this.diplomes.forEach(i -> i.setFiliereEtude(null));
        }
        if (diplomes != null) {
            diplomes.forEach(i -> i.setFiliereEtude(this));
        }
        this.diplomes = diplomes;
    }

    public FiliereEtude diplomes(Set<Diplome> diplomes) {
        this.setDiplomes(diplomes);
        return this;
    }

    public FiliereEtude addDiplome(Diplome diplome) {
        this.diplomes.add(diplome);
        diplome.setFiliereEtude(this);
        return this;
    }

    public FiliereEtude removeDiplome(Diplome diplome) {
        this.diplomes.remove(diplome);
        diplome.setFiliereEtude(null);
        return this;
    }

    public Set<ReleveNote> getReleveNotes() {
        return this.releveNotes;
    }

    public void setReleveNotes(Set<ReleveNote> releveNotes) {
        if (this.releveNotes != null) {
            this.releveNotes.forEach(i -> i.setFiliereEtude(null));
        }
        if (releveNotes != null) {
            releveNotes.forEach(i -> i.setFiliereEtude(this));
        }
        this.releveNotes = releveNotes;
    }

    public FiliereEtude releveNotes(Set<ReleveNote> releveNotes) {
        this.setReleveNotes(releveNotes);
        return this;
    }

    public FiliereEtude addReleveNote(ReleveNote releveNote) {
        this.releveNotes.add(releveNote);
        releveNote.setFiliereEtude(this);
        return this;
    }

    public FiliereEtude removeReleveNote(ReleveNote releveNote) {
        this.releveNotes.remove(releveNote);
        releveNote.setFiliereEtude(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiliereEtude)) {
            return false;
        }
        return id != null && id.equals(((FiliereEtude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiliereEtude{" +
            "id=" + getId() +
            ", filiere='" + getFiliere() + "'" +
            "}";
    }
}
