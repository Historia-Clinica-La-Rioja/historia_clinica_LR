package ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty;

import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferenceClinicalSpecialtyRepository extends JpaRepository<ReferenceClinicalSpecialty, ReferenceClinicalSpecialtyPk> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo(cs.id, cs.name) " +
			"FROM ReferenceClinicalSpecialty rcs " +
			"JOIN ClinicalSpecialty cs ON (cs.id = rcs.pk.clinicalSpecialtyId)" +
			"WHERE rcs.pk.referenceId = :referenceId")
	List<ClinicalSpecialtyBo> getClinicalSpecialtiesByReferenceId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query(" SELECT cs.name " +
			"FROM ReferenceClinicalSpecialty rcs " +
			"JOIN ClinicalSpecialty cs ON (cs.id = rcs.pk.clinicalSpecialtyId) " +
			"WHERE rcs.pk.referenceId = :referenceId")
	List<String> getClinicalSpecialtyNamesByReferenceId(@Param("referenceId") Integer referenceId);

}
