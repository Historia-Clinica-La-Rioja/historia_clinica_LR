package net.pladema.electronicjointsignature.documentlist.infrastructure.output;

import lombok.AllArgsConstructor;

import net.pladema.electronicjointsignature.documentlist.infrastructure.output.vo.ElectronicSignatureDocumentProblemsVo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class GetProfessionalInvolvedDocumentListStorage {

	private EntityManager entityManager;

	public List<ElectronicSignatureInvolvedDocumentBo> run(Integer institutionId, Integer healthcareProfessionalId) {
		String queryString = "SELECT NEW net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo(d.id, d.typeId, p2.personId, up.pk.personId, " +
				"d.creationable.createdOn, dip.signatureStatusId, dip.statusUpdateDate) " +
				"FROM DocumentInvolvedProfessional dip " +
				"JOIN Document d ON (d.id = dip.documentId) " +
				"JOIN UserPerson up ON (up.pk.userId = d.creationable.createdBy) " +
				"JOIN Patient p2 ON (p2.id = d.patientId) " +
				"WHERE d.institutionId = :institutionId " +
				"AND dip.healthcareProfessionalId = :healthcareProfessionalId";
		Query query = parseDocumentQuery(institutionId, healthcareProfessionalId, queryString);
		List<ElectronicSignatureInvolvedDocumentBo> result = getElectronicSignatureInvolvedDocumentBos(query);
		return result;
	}

	private List<ElectronicSignatureInvolvedDocumentBo> getElectronicSignatureInvolvedDocumentBos(Query query) {
		List<ElectronicSignatureInvolvedDocumentBo> result = query.getResultList();
		fetchDocumentProblems(result);
		return result;
	}

	private void fetchDocumentProblems(List<ElectronicSignatureInvolvedDocumentBo> documents) {
		String queryString = "SELECT NEW net.pladema.electronicjointsignature.documentlist.infrastructure.output.vo.ElectronicSignatureDocumentProblemsVo(d.id, s.pt) " +
				"FROM Document d " +
				"JOIN DocumentHealthCondition dhc ON (dhc.pk.documentId = d.id) " +
				"JOIN HealthCondition hc ON (hc.id = dhc.pk.healthConditionId) " +
				"JOIN Snomed s ON (s.id = hc.snomedId) " +
				"WHERE d.id IN :documentIds";
		Query query = parseDocumentProblemsQuery(documents, queryString);
		List<ElectronicSignatureDocumentProblemsVo> documentProblems = query.getResultList();
		documents.forEach(document -> setDocumentProblems(document, documentProblems));
	}

	private void setDocumentProblems(ElectronicSignatureInvolvedDocumentBo document, List<ElectronicSignatureDocumentProblemsVo> problems) {
		List<String> documentRelatedProblems = problems.stream()
				.filter(problem -> problem.getDocumentId().equals(document.getDocumentId()))
				.map(ElectronicSignatureDocumentProblemsVo::getProblem).collect(Collectors.toList());
		document.setProblems(documentRelatedProblems);
	}

	private Query parseDocumentProblemsQuery(List<ElectronicSignatureInvolvedDocumentBo> documents, String queryString) {
		List<Long> documentIds = documents.stream().map(ElectronicSignatureInvolvedDocumentBo::getDocumentId).collect(Collectors.toList());
		Query result = entityManager.createQuery(queryString);
		result.setParameter("documentIds", documentIds);
		return result;
	}

	private Query parseDocumentQuery(Integer institutionId, Integer healthcareProfessionalId, String queryString) {
		Query result = entityManager.createQuery(queryString);
		result.setParameter("institutionId", institutionId);
		result.setParameter("healthcareProfessionalId", healthcareProfessionalId);
		return result;
	}

}
