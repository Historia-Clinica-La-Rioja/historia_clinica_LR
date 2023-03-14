package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.VReason;

@Repository
public interface VReasonsRepository extends JpaRepository<VReason, String> {

	@Transactional(readOnly = true)
	@Query("SELECT vr " +
			"FROM VReason vr " +
			"WHERE vr.documentId = :documentId ")
	List<VReason> fetchFromDocumentId(@Param("documentId") Long documentId);
}
