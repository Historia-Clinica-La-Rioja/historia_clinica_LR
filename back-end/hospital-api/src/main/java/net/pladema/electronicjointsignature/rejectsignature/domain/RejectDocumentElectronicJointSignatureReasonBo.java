package net.pladema.electronicjointsignature.rejectsignature.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class RejectDocumentElectronicJointSignatureReasonBo {

	private Short rejectReasonId;

	private String description;

}
