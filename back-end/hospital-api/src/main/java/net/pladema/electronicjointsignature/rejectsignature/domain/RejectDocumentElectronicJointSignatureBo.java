package net.pladema.electronicjointsignature.rejectsignature.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RejectDocumentElectronicJointSignatureBo {

	private List<Long> documentIds;

	private Integer healthcareProfessionalId;

	private RejectDocumentElectronicJointSignatureReasonBo reason;

}
