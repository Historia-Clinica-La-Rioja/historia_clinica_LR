package net.pladema.electronicjointsignature.rejectsignature.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RejectElectronicJointSignatureDocumentInvolvedProfessionalBo {

	private Integer documentInvolvedProfessionalId;

	private Short statusId;

	private LocalDate statusUpdateDate;

}
