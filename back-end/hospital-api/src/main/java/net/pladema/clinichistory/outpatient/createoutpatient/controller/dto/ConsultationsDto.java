package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ConsultationsDto implements Serializable {

	private Integer id;

	private LocalDate consultationDate;

	private String specialty;

	private String completeProfessionalName;
}
