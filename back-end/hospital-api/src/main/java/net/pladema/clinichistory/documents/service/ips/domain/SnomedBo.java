package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import net.pladema.sgx.exceptions.SelfValidating;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedBo extends SelfValidating<SnomedBo> {

    private Integer id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 20, message = "{snomed.id.max.value}")
    private String sctid;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 255, message = "{snomed.pt.max.value}")
    private String pt;

    private String parentId;

    private String parentFsn;

    public SnomedBo(Snomed snomed) {
        this.sctid = snomed.getSctid();
        this.pt = snomed.getPt();
        this.parentId = snomed.getParentId();
        this.parentFsn = snomed.getParentFsn();
    }

    public SnomedBo(String sctid, String pt) {
        this.sctid = sctid;
        this.pt = pt;
        this.parentId = sctid;
        this.parentFsn = pt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SnomedBo)) return false;
        SnomedBo snomedBo = (SnomedBo) o;
        return Objects.equals(getSctid(), snomedBo.getSctid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSctid());
    }
}
