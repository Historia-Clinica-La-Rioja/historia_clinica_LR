package net.pladema.electronicjointsignature.documentlist.infrastructure.output.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ElectronicSignatureDocumentProblemsVo {

	private Long documentId;

	private String problem;

}
