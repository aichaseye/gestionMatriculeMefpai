package sn.seye.gesmat.mefpai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.seye.gesmat.mefpai.domain.enumeration.NomPays;

/**
 * A Pays.
 */
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom_pays", nullable = false)
    private NomPays nomPays;

    @OneToMany(mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departements", "pays" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pays id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomPays getNomPays() {
        return this.nomPays;
    }

    public Pays nomPays(NomPays nomPays) {
        this.setNomPays(nomPays);
        return this;
    }

    public void setNomPays(NomPays nomPays) {
        this.nomPays = nomPays;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public void setRegions(Set<Region> regions) {
        if (this.regions != null) {
            this.regions.forEach(i -> i.setPays(null));
        }
        if (regions != null) {
            regions.forEach(i -> i.setPays(this));
        }
        this.regions = regions;
    }

    public Pays regions(Set<Region> regions) {
        this.setRegions(regions);
        return this;
    }

    public Pays addRegion(Region region) {
        this.regions.add(region);
        region.setPays(this);
        return this;
    }

    public Pays removeRegion(Region region) {
        this.regions.remove(region);
        region.setPays(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pays)) {
            return false;
        }
        return id != null && id.equals(((Pays) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", nomPays='" + getNomPays() + "'" +
            "}";
    }
}
