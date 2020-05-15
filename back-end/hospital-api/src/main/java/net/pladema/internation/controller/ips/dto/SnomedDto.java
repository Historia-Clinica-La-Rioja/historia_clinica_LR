package net.pladema.internation.controller.ips.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
