package net.pladema.electronicjointsignature.professionalsstatus.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DocumentElectronicSignatureProfessionalStatusBo {

	private Integer personId;

	private String professionalCompleteName;

	private Short statusId;

	private LocalDate date;

	public DocumentElectronicSignatureProfessionalStatusBo(Integer personId, Short statusId, LocalDate date) {
		this.personId = personId;
		this.statusId = statusId;
		this.date = date;
	}

}
