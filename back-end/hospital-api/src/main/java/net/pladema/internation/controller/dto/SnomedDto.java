package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class SnomedDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 20, message = "{snomed.id.max.value}")
    private String id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 100, message = "{snomed.pt.max.value}")
    private String pt;

    private String parentId;

    private String parentFsn;
}
