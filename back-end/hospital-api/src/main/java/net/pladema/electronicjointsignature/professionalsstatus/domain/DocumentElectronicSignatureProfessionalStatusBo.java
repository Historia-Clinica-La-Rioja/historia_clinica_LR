package net.pladema.electronicjointsignature.professionalsstatus.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DocumentElectronicSignatureProfessionalStatusBo {

	private Integer documentInvolvedProfessionalId;

	private Integer personId;

	private String professionalCompleteName;

	private Short statusId;

	private LocalDate date;

	public DocumentElectronicSignatureProfessionalStatusBo(Integer documentInvolvedProfessionalId, Integer personId, Short statusId, LocalDate date) {
		this.documentInvolvedProfessionalId = documentInvolvedProfessionalId;
		this.personId = personId;
		this.statusId = statusId;
		this.date = date;
	}

}
