package net.pladema.reports.controller.dto;

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

	private Long documentId;

	private LocalDate consultationDate;

	private String specialty;

	private String completeProfessionalName;
}
