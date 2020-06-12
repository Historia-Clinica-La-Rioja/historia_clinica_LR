package net.pladema.internation.service.ips.domain;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedBo implements Serializable {

    @NotNull
    @NotEmpty
    private String id;

    @NotNull
    @NotEmpty
    private String pt;

    private String parentId;

    private String parentFsn;

    public SnomedBo(Snomed snomed) {
        this.id = snomed.getId();
        this.pt = snomed.getPt();
        this.parentId = snomed.getParentId();
        this.parentFsn = snomed.getParentFsn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SnomedBo)) return false;
        SnomedBo snomedBo = (SnomedBo) o;
        return Objects.equals(getId(), snomedBo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
