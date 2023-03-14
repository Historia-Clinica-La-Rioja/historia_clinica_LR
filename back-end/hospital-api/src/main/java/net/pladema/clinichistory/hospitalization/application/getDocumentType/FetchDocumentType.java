package net.pladema.clinichistory.hospitalization.application.getDocumentType;

import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;

import java.util.List;

public interface FetchDocumentType {

	List<DocumentTypeBo> run();
}
