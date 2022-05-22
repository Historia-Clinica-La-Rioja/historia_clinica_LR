package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewDosageDto implements Serializable {

    @Nullable
    private Integer frequency;

    @NotNull(message = "{value.mandatory}")
    private boolean diary;

    @NotNull(message = "{value.mandatory}")
    private boolean chronic;

    @Nullable
    private Double duration;

	@Nullable
	private String periodUnit;

	@Nullable
	private String event;

	@Nullable
	private DateTimeDto startDateTime;

	@Nullable
	private QuantityDto quantity;

}
