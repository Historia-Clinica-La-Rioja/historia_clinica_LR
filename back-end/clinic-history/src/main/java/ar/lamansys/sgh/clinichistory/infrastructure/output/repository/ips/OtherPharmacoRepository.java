package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherPharmaco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OtherPharmacoRepository extends JpaRepository<OtherPharmaco, Integer> {

	@Transactional(readOnly=true)
	@Query("SELECT op "
		+ "FROM OtherPharmaco op "
		+ "WHERE op.indicationId = :indicationId ")
	List<OtherPharmaco> getByIndicationId (@Param("indicationId") Integer indicationId);
}
