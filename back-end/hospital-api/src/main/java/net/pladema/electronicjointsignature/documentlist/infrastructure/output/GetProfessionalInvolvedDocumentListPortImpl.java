package net.pladema.electronicjointsignature.documentlist.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.documentlist.application.port.GetProfessionalInvolvedDocumentListPort;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GetProfessionalInvolvedDocumentListPortImpl implements GetProfessionalInvolvedDocumentListPort {

	private GetProfessionalInvolvedDocumentListStorage getProfessionalInvolvedDocumentListStorage;

	@Override
	public List<ElectronicSignatureInvolvedDocumentBo> run(Integer institutionId, Integer healthcareProfessionalId) {
		return getProfessionalInvolvedDocumentListStorage.run(institutionId, healthcareProfessionalId);
	}

}
