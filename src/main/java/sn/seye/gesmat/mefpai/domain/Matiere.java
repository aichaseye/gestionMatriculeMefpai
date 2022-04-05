package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_matiere")
    private String nomMatiere;

    @Column(name = "reference")
    private String reference;

    @Column(name = "type_matiere")
    private String typeMatiere;

    @Column(name = "matricule_matiere")
    private String matriculeMatiere;

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "matiere" }, allowSetters = true)
    private Set<Bon> bons = new HashSet<>();

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matiere" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMatiere() {
        return this.nomMatiere;
    }

    public Matiere nomMatiere(String nomMatiere) {
        this.setNomMatiere(nomMatiere);
        return this;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getReference() {
        return this.reference;
    }

    public Matiere reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTypeMatiere() {
        return this.typeMatiere;
    }

    public Matiere typeMatiere(String typeMatiere) {
        this.setTypeMatiere(typeMatiere);
        return this;
    }

    public void setTypeMatiere(String typeMatiere) {
        this.typeMatiere = typeMatiere;
    }

    public String getMatriculeMatiere() {
        return this.matriculeMatiere;
    }

    public Matiere matriculeMatiere(String matriculeMatiere) {
        this.setMatriculeMatiere(matriculeMatiere);
        return this;
    }

    public void setMatriculeMatiere(String matriculeMatiere) {
        this.matriculeMatiere = matriculeMatiere;
    }

    public Set<Bon> getBons() {
        return this.bons;
    }

    public void setBons(Set<Bon> bons) {
        if (this.bons != null) {
            this.bons.forEach(i -> i.setMatiere(null));
        }
        if (bons != null) {
            bons.forEach(i -> i.setMatiere(this));
        }
        this.bons = bons;
    }

    public Matiere bons(Set<Bon> bons) {
        this.setBons(bons);
        return this;
    }

    public Matiere addBon(Bon bon) {
        this.bons.add(bon);
        bon.setMatiere(this);
        return this;
    }

    public Matiere removeBon(Bon bon) {
        this.bons.remove(bon);
        bon.setMatiere(null);
        return this;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setMatiere(null));
        }
        if (images != null) {
            images.forEach(i -> i.setMatiere(this));
        }
        this.images = images;
    }

    public Matiere images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Matiere addImage(Image image) {
        this.images.add(image);
        image.setMatiere(this);
        return this;
    }

    public Matiere removeImage(Image image) {
        this.images.remove(image);
        image.setMatiere(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return id != null && id.equals(((Matiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", nomMatiere='" + getNomMatiere() + "'" +
            ", reference='" + getReference() + "'" +
            ", typeMatiere='" + getTypeMatiere() + "'" +
            ", matriculeMatiere='" + getMatriculeMatiere() + "'" +
            "}";
    }
}
