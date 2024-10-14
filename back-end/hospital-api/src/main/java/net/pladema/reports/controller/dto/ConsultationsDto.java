package net.pladema.reports.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ConsultationsDto implements Serializable {

	private Integer id;

	private Long documentId;

	private DateTimeDto consultationDate;

	private String specialty;

	private String completeProfessionalName;
}
