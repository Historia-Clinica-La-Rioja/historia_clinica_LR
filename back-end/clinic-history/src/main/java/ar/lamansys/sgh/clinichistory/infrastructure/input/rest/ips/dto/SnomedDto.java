package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import javax.annotation.Nullable;
import lombok.*;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SnomedDto implements Serializable {

    @Nullable
    private Integer id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 20, message = "{snomed.id.max.value}")
    @EqualsAndHashCode.Include
    private String sctid;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 400, message = "{snomed.pt.max.value}")
    private String pt;

    @Nullable
    private String parentId;

    @Nullable
    private String parentFsn;

    public SnomedDto(@NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 20, message = "{snomed.id.max.value}") String sctid,
                     @NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 400, message = "{snomed.pt.max.value}") String pt) {
        this.sctid = sctid;
        this.pt = pt;
    }

    public static SnomedDto from(SnomedBo snomed) {
        if (snomed == null)
            return null;
        SnomedDto result = new SnomedDto();
        result.setId(snomed.getId());
        result.setSctid(snomed.getSctid());
        result.setPt(snomed.getPt());
        return result;
    }
}
