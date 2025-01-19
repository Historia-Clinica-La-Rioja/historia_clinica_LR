package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.output.MedicationStatementPort;

import ar.lamansys.sgh.clinichistory.domain.CommercialPrescriptionDataBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MedicationStatementPortImpl implements MedicationStatementPort {

	private final MedicationStatementRepository medicationStatementRepository;

	@Override
	public Short fetchMedicationStatementLineStateById(Integer medicationStatementId) {
		return medicationStatementRepository.fetchMedicationStatementLineStateById(medicationStatementId);
	}

	@Override
	public Double getMedicationStatementQuantityById(Integer medicationStatementId) {
		return medicationStatementRepository.fetchMedicationStatementQuantityById(medicationStatementId);
	}

	@Override
	public List<CommercialPrescriptionDataBo> getCommercialPrescriptionDataByIds(List<Integer> medicationStatementIds) {
		return medicationStatementRepository.fetchCommercialPrescriptionDataByIds(medicationStatementIds);
	}

}
