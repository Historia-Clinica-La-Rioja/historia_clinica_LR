package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IndicationDto {

	private Integer id;

	@NotNull(message = "{value.mandatory}")
	private Integer patientId;

	@NotNull(message = "{value.mandatory}")
	private EIndicationType type;

	@NotNull(message = "{value.mandatory}")
	private EIndicationStatus status;

	private Integer createdBy;

	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	private LocalDateTime indicationDate;
	
}
