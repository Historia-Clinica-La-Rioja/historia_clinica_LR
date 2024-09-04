package net.pladema.medicine.application.port;

import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.domain.MedicineFinancingStatusBo;

import net.pladema.medicine.domain.MedicineFinancingStatusFilterBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicineFinancingStatusSearchStorage {

	Page<MedicineFinancingStatusBo> findAllByFilter(MedicineFinancingStatusFilterBo filter, Pageable pageable);

	Page<InstitutionMedicineFinancingStatusBo> findAllByFilter(Integer institutionId, MedicineFinancingStatusFilterBo filter, Pageable pageable);

}
