package ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port;

import java.util.List;

public interface DocumentInvolvedProfessionalStorage {

	List<Integer> fetchSignerInvolvedProfessionalIdsByDocumentId(Long documentId);

}
