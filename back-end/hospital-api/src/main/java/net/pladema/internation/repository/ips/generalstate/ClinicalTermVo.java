package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClinicalTermVo {

    private Integer id;

    private Snomed snomed;

    private String statusId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicalTermVo that = (ClinicalTermVo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
