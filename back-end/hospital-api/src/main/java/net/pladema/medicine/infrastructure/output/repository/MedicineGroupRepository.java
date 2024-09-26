package net.pladema.medicine.infrastructure.output.repository;


import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicineGroupRepository extends SGXAuditableEntityJPARepository<MedicineGroup, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mg " +
			"FROM MedicineGroup mg " +
			"WHERE mg.name = :name " +
			"AND mg.deleteable.deleted IS FALSE")
	List<MedicineGroup> findGroupsByName(@Param("name") String name);


	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineGroupBo(" +
			"mg.name, mg.requiredDocumentation) " +
			"FROM MedicineGroup mg " +
			"LEFT JOIN MedicineGroupMedicine mgm ON (mg.id = mgm.medicineGroupId)" +
			"LEFT JOIN Snomed s2 ON (s2.id = mgm.medicineId)" +
			"LEFT JOIN MedicineGroupProblem mgp ON (mg.id = mgp.medicineGroupId)" +
			"LEFT JOIN Snomed s ON (s.id = mgp.problemId)" +
			"WHERE (s2.sctid = :medicineId " +
			"AND s.sctid = :problemId " +
			"AND mg.requiresAudit = true " +
			"AND mgp.deleteable.deleted IS FALSE " +
			"AND mgm.deleteable.deleted IS FALSE " +
			"AND mg.deleteable.deleted IS FALSE) " +
			"OR (s2.sctid = :medicineId " +
			"AND mgp.problemId is null " +
			"AND mg.allDiagnoses = true " +
			"AND mgp.deleteable.deleted IS FALSE " +
			"AND mg.deleteable.deleted IS FALSE " +
			"AND mg.requiresAudit = true) ")
	List<InstitutionMedicineGroupBo> getAllMedicineGroupByProblemAndMedicine(@Param("problemId") String problemId,
																			 @Param("medicineId") String medicineId);

}
