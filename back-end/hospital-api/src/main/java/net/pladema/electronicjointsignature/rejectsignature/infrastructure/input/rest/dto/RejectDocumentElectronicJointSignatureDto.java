package net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.electronicjointsignature.rejectsignature.domain.enums.ERejectDocumentElectronicJointSignatureReason;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class RejectDocumentElectronicJointSignatureDto {

	private List<Long> documentIds;

	private ERejectDocumentElectronicJointSignatureReason rejectReason;

	private String description;

}
