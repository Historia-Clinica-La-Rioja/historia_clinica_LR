package net.pladema.medicine.infrastructure.output.repository;

import net.pladema.medicine.domain.MedicineFinancingStatusBo;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineFinancingStatus;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineFinancingStatusRepository extends SGXAuditableEntityJPARepository<MedicineFinancingStatus, Integer> {

	@Query("SELECT NEW net.pladema.medicine.domain.MedicineFinancingStatusBo(m.id, s.sctid, s.pt, m.financed) " +
			"FROM MedicineFinancingStatus m " +
			"JOIN Snomed s ON (m.id = s.id) " +
			"WHERE m.deleteable.deleted IS FALSE " +
			"ORDER BY s.pt ASC")
	List<MedicineFinancingStatusBo> getAll();

	@Query("SELECT NEW net.pladema.medicine.domain.MedicineFinancingStatusBo(m.id, s.sctid, s.pt, m.financed) " +
			"FROM MedicineFinancingStatus m " +
			"JOIN Snomed s ON (m.id = s.id) " +
			"WHERE m.id IN :ids " +
			"ORDER BY s.pt ASC")
	List<MedicineFinancingStatusBo> getAllById(@Param("ids") List<Integer> ids);

	@Query("SELECT NEW net.pladema.medicine.domain.MedicineFinancingStatusBo(m.id, s.sctid, s.pt, m.financed) " +
			"FROM MedicineFinancingStatus m " +
			"JOIN Snomed s ON (m.id = s.id) " +
			"WHERE m.id = :id")
	Optional<MedicineFinancingStatusBo> findMedicineById(@Param("id") Integer id);

}
