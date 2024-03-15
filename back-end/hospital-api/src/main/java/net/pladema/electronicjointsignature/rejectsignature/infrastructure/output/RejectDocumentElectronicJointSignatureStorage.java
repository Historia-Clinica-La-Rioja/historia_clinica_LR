package net.pladema.electronicjointsignature.rejectsignature.infrastructure.output;

import lombok.AllArgsConstructor;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectElectronicJointSignatureDocumentInvolvedProfessionalBo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class RejectDocumentElectronicJointSignatureStorage {

	private EntityManager entityManager;

	public Optional<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> fetchSignatureData(Long documentId, Integer healthcareProfessionalId) {
		String queryString = "SELECT NEW net.pladema.electronicjointsignature.rejectsignature.domain.RejectElectronicJointSignatureDocumentInvolvedProfessionalBo(dip.id, dip.signatureStatusId, dip.statusUpdateDate) " +
				"FROM DocumentInvolvedProfessional dip " +
				"WHERE dip.documentId = :documentId " +
				"AND dip.healthcareProfessionalId = :healthcareProfessionalId";
		Query query = getQuery(queryString, documentId, healthcareProfessionalId);
		List<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> result = query.getResultList();
		return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
	}

	private Query getQuery(String queryString, Long documentId, Integer healthcareProfessionalId) {
		Query result = entityManager.createQuery(queryString);
		result.setParameter("documentId", documentId);
		result.setParameter("healthcareProfessionalId", healthcareProfessionalId);
		return result;
	}

}
