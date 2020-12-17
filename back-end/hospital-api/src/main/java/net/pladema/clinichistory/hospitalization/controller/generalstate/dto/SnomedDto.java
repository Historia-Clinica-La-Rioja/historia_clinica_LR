package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import com.sun.istack.Nullable;
import lombok.*;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
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

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 20, message = "{snomed.id.max.value}")
    @EqualsAndHashCode.Include
    private String id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 255, message = "{snomed.pt.max.value}")
    private String pt;

    @Nullable
    private String parentId;

    @Nullable
    private String parentFsn;

    public SnomedDto(@NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 20, message = "{snomed.id.max.value}") String id,
                     @NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 255, message = "{snomed.pt.max.value}") String pt) {
        this.id = id;
        this.pt = pt;
    }

    public static SnomedDto from(SnomedBo snomed) {
        if (snomed == null)
            return null;
        SnomedDto result = new SnomedDto();
        result.setId(snomed.getId());
        result.setPt(snomed.getPt());
        return result;
    }
}
