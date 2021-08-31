package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class HCEClinicalTermDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3502940404811842839L;

    @Nullable
	private Integer id;

    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

}