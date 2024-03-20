package net.pladema.electronicjointsignature.professionalsstatus.infrastructure.output;

import lombok.AllArgsConstructor;

import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

@AllArgsConstructor
@Repository
public class DocumentElectronicSignatureProfessionalStatusStorage {

	private EntityManager entityManager;

	public List<DocumentElectronicSignatureProfessionalStatusBo> fetch(Long documentId) {
		String queryString = "SELECT NEW net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo(dip.id, hp.personId, " +
				"dip.signatureStatusId, dip.statusUpdateDate) " +
				"FROM DocumentInvolvedProfessional dip " +
				"JOIN HealthcareProfessional hp ON (hp.id = dip.healthcareProfessionalId) " +
				"WHERE dip.documentId = :documentId";
		Query query = createQuery(documentId, queryString);
		List<DocumentElectronicSignatureProfessionalStatusBo> result = query.getResultList();
		return result;
	}

	private Query createQuery(Long documentId, String queryString) {
		Query query = entityManager.createQuery(queryString);
		query.setParameter("documentId", documentId);
		return query;
	}

}
