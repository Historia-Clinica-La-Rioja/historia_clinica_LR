package net.pladema.medicine.infrastructure.output.repository;


import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
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


}
