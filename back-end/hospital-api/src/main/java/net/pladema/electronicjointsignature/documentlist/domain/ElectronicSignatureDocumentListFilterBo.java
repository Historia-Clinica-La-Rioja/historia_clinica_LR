package net.pladema.electronicjointsignature.documentlist.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class ElectronicSignatureDocumentListFilterBo {

	private Integer institutionId;

	private Integer healthcareProfessionalId;

	private List<Short> signatureStatusIds;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private String patientFirstName;

	private String patientLastName;

	private boolean isSelfDeterminationNameFFActive;

}
