package ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port;

import java.util.List;

public interface DocumentInvolvedProfessionalStorage {

	List<Integer> fetchSignerInvolvedProfessionalIdsByDocumentId(Long documentId);

	List<Integer> getDocumentInvolvedProfessionalPersonIdsByDocumentIdAndStatusId(Long documentId, Short id);

	Integer getDocumentInvolvedProfessionalAmountThatDidNotSignByDocumentId(Long documentId, Short pendingStatusId);
}
