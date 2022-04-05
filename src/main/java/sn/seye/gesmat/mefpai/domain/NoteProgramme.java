package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NoteProgramme.
 */
@Entity
@Table(name = "note_programme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NoteProgramme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_prog")
    private String nomProg;

    @Column(name = "coef")
    private Integer coef;

    @Column(name = "note")
    private Float note;

    @ManyToMany
    @JoinTable(
        name = "rel_note_programme__releve_note",
        joinColumns = @JoinColumn(name = "note_programme_id"),
        inverseJoinColumns = @JoinColumn(name = "releve_note_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "filiereEtude", "serieEtude", "niveauEtude", "noteProgrammes" }, allowSetters = true)
    private Set<ReleveNote> releveNotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NoteProgramme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProg() {
        return this.nomProg;
    }

    public NoteProgramme nomProg(String nomProg) {
        this.setNomProg(nomProg);
        return this;
    }

    public void setNomProg(String nomProg) {
        this.nomProg = nomProg;
    }

    public Integer getCoef() {
        return this.coef;
    }

    public NoteProgramme coef(Integer coef) {
        this.setCoef(coef);
        return this;
    }

    public void setCoef(Integer coef) {
        this.coef = coef;
    }

    public Float getNote() {
        return this.note;
    }

    public NoteProgramme note(Float note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Set<ReleveNote> getReleveNotes() {
        return this.releveNotes;
    }

    public void setReleveNotes(Set<ReleveNote> releveNotes) {
        this.releveNotes = releveNotes;
    }

    public NoteProgramme releveNotes(Set<ReleveNote> releveNotes) {
        this.setReleveNotes(releveNotes);
        return this;
    }

    public NoteProgramme addReleveNote(ReleveNote releveNote) {
        this.releveNotes.add(releveNote);
        releveNote.getNoteProgrammes().add(this);
        return this;
    }

    public NoteProgramme removeReleveNote(ReleveNote releveNote) {
        this.releveNotes.remove(releveNote);
        releveNote.getNoteProgrammes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteProgramme)) {
            return false;
        }
        return id != null && id.equals(((NoteProgramme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteProgramme{" +
            "id=" + getId() +
            ", nomProg='" + getNomProg() + "'" +
            ", coef=" + getCoef() +
            ", note=" + getNote() +
            "}";
    }
}
