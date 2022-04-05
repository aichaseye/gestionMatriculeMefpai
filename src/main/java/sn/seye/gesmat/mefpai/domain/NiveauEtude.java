package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.Niveau;

/**
 * A NiveauEtude.
 */
@Entity
@Table(name = "niveau_etude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NiveauEtude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private Niveau niveau;

    @OneToMany(mappedBy = "niveauEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude" }, allowSetters = true)
    private Set<Diplome> diplomes = new HashSet<>();

    @OneToMany(mappedBy = "niveauEtude")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude", "noteProgrammes" }, allowSetters = true)
    private Set<ReleveNote> releveNotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NiveauEtude id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public NiveauEtude niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Set<Diplome> getDiplomes() {
        return this.diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        if (this.diplomes != null) {
            this.diplomes.forEach(i -> i.setNiveauEtude(null));
        }
        if (diplomes != null) {
            diplomes.forEach(i -> i.setNiveauEtude(this));
        }
        this.diplomes = diplomes;
    }

    public NiveauEtude diplomes(Set<Diplome> diplomes) {
        this.setDiplomes(diplomes);
        return this;
    }

    public NiveauEtude addDiplome(Diplome diplome) {
        this.diplomes.add(diplome);
        diplome.setNiveauEtude(this);
        return this;
    }

    public NiveauEtude removeDiplome(Diplome diplome) {
        this.diplomes.remove(diplome);
        diplome.setNiveauEtude(null);
        return this;
    }

    public Set<ReleveNote> getReleveNotes() {
        return this.releveNotes;
    }

    public void setReleveNotes(Set<ReleveNote> releveNotes) {
        if (this.releveNotes != null) {
            this.releveNotes.forEach(i -> i.setNiveauEtude(null));
        }
        if (releveNotes != null) {
            releveNotes.forEach(i -> i.setNiveauEtude(this));
        }
        this.releveNotes = releveNotes;
    }

    public NiveauEtude releveNotes(Set<ReleveNote> releveNotes) {
        this.setReleveNotes(releveNotes);
        return this;
    }

    public NiveauEtude addReleveNote(ReleveNote releveNote) {
        this.releveNotes.add(releveNote);
        releveNote.setNiveauEtude(this);
        return this;
    }

    public NiveauEtude removeReleveNote(ReleveNote releveNote) {
        this.releveNotes.remove(releveNote);
        releveNote.setNiveauEtude(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NiveauEtude)) {
            return false;
        }
        return id != null && id.equals(((NiveauEtude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NiveauEtude{" +
            "id=" + getId() +
            ", niveau='" + getNiveau() + "'" +
            "}";
    }
}
