package net.pladema.medicine.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.medicine.domain.MedicineGroupProblemBo;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroupProblem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineGroupProblemRepository extends SGXAuditableEntityJPARepository<MedicineGroupProblem, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.MedicineGroupProblemBo(mgp.id, mgp.medicineGroupId, s.id, s.pt) " +
			"FROM MedicineGroupProblem mgp " +
			"JOIN Snomed s ON (mgp.problemId = s.id) " +
			"WHERE mgp.medicineGroupId = :medicineGroupId " +
			"AND mgp.deleteable.deleted IS FALSE ")
	List<MedicineGroupProblemBo> getByMedicineGroupId(@Param("medicineGroupId") Integer medicineGroupId);


	@Transactional(readOnly = true)
	@Query("SELECT mgp " +
			"FROM MedicineGroupProblem mgp " +
			"WHERE mgp.medicineGroupId = :medicineGroupId " +
			"AND mgp.problemId = :problemId " +
			"AND mgp.deleteable.deleted IS FALSE")
	Optional<MedicineGroupProblem> getByGroupIdAndProblemId(@Param("medicineGroupId") Integer medicineGroupId, @Param("problemId")Integer problemId);


}
