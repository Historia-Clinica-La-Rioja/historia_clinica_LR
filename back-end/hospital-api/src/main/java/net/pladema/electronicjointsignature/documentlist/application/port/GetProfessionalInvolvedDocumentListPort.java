package net.pladema.electronicjointsignature.documentlist.application.port;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProfessionalInvolvedDocumentListPort {

	Page<ElectronicSignatureInvolvedDocumentBo> fetchProfessionalInvolvedDocuments(ElectronicSignatureDocumentListFilterBo filter, Pageable pageable);

}
