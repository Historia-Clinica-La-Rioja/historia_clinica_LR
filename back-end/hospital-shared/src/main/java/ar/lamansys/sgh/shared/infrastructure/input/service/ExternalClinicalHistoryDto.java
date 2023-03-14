package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalClinicalHistoryDto {

	@NotNull
	private Short patientGender;

	@NotNull
	private Short patientDocumentType;

	@NotNull
	private String patientDocumentNumber;

	@NotNull
	private String notes;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_FORMAT)
	private LocalDate consultationDate;

	@Nullable
	private String institution;

	@Nullable
	private String professionalName;

	@Nullable
	private String professionalSpecialty;

}
