package net.pladema.establishment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Embeddable
public class VInstitutionPK implements Serializable {

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "internment_episode_id", nullable = false)
    private Integer internmentEpisodeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VInstitutionPK that = (VInstitutionPK) o;
        return institutionId.equals(that.institutionId) &&
                internmentEpisodeId.equals(that.internmentEpisodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(institutionId, internmentEpisodeId);
    }
}
