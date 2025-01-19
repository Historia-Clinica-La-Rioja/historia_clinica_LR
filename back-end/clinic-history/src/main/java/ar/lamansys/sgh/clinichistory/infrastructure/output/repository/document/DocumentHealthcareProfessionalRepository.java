package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthcareProfessional;

@Repository
public interface DocumentHealthcareProfessionalRepository extends JpaRepository<DocumentHealthcareProfessional, Integer> {

	@Query("SELECT new ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo(" +
			"dhp.id, hp.id, hp.licenseNumber, p.id, p.firstName, p.lastName, p.identificationNumber, pex.nameSelfDetermination," +
			"p.middleNames, p.otherLastNames, " +
			"dhp.professionTypeId, dhp.otherProfessionTypeDescription, dhp.comments) " +
			"FROM DocumentHealthcareProfessional dhp " +
			"JOIN HealthcareProfessional hp ON (hp.id = dhp.healthcareProfessionalId) " +
			"JOIN Person p ON (p.id = hp.personId) " +
			"JOIN PersonExtended pex ON (pex.id = p.id) " +
			"WHERE dhp.documentId = :documentId")
	List<DocumentHealthcareProfessionalBo> getFromDocument(@Param("documentId") Long documentId);
}