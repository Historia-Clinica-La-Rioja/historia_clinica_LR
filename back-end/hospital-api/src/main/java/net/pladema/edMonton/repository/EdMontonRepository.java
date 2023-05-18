package net.pladema.edMonton.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.EnableHospitalLib;
import net.pladema.edMonton.get.controller.dto.EdMontonSummary;
import net.pladema.edMonton.repository.domain.Answer;
import net.pladema.edMonton.repository.domain.QuestionnaireResponse;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EdMontonRepository extends SGXAuditableEntityJPARepository<QuestionnaireResponse, Integer> {

	@Query(value = "SELECT new net.pladema.edMonton.repository.domain.Answer(la.id, la.itemId, la.questionnaireResponseId, la.answerId) " +
			"FROM Answer la " +
			"INNER JOIN QuestionnaireResponse qr ON  qr.id = la.questionnaireResponseId " +
			"WHERE qr.patientId = :patientId ")
	public List<Answer> findPatientEdMontonTest(@Param("patientId") Integer patientId);

	@Query(value = "SELECT new net.pladema.edMonton.get.controller.dto.EdMontonSummary( p.firstName, p.middleNames, p.lastName, hp.licenseNumber, qr.createdOn) " +
			"FROM QuestionnaireResponse qr " +
			"INNER JOIN SharedHealthcareProfessional hp ON hp.id = qr.createdBy " +
			"INNER JOIN Person p ON p.id = hp.personId " +
			"WHERE qr.id = :edMontonId ")
	public EdMontonSummary findEdMontonSummary(@Param("edMontonId") Integer edMontonId);


}