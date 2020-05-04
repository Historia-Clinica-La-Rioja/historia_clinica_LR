package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.SnomedDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class ClinicalTermDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3502940404811842839L;

	private Integer id;

    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

}
