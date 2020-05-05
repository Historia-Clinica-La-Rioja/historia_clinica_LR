package net.pladema.internation.service.ips.domain;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
}
