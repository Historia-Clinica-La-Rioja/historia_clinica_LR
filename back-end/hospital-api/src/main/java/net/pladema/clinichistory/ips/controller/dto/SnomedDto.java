package net.pladema.clinichistory.ips.controller.dto;

import lombok.*;
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
    @NotEmpty
    @Length(max = 20, message = "{snomed.id.max.value}")
    @EqualsAndHashCode.Include
    private String id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 255, message = "{snomed.pt.max.value}")
    private String pt;

    private String parentId;

    private String parentFsn;


}
