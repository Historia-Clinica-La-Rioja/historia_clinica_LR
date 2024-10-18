package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationUpdateFilePort;

import net.pladema.medication.infrastructure.repository.CommercialMedicationUpdateFileRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommercialMedicationUpdateFilePortImpl implements CommercialMedicationUpdateFilePort {

	private final CommercialMedicationUpdateFileRepository commercialMedicationUpdateFileRepository;

	@Override
	public Long getLastNonProcessedLogId() {
		return commercialMedicationUpdateFileRepository.fetchLastNonProcessedLogId();
	}

	@Override
	public void saveNewEntry(Long logId) {
		commercialMedicationUpdateFileRepository.save(new CommercialMedicationUpdateFile(logId));
	}

}
