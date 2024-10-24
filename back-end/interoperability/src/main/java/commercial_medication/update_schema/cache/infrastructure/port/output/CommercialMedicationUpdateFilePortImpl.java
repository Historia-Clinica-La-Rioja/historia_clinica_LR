package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationUpdateFilePort;

import commercial_medication.update_schema.cache.domain.CommercialMedicationFileUpdateBo;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationUpdateFileRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommercialMedicationUpdateFilePortImpl implements CommercialMedicationUpdateFilePort {

	private final CommercialMedicationUpdateFileRepository commercialMedicationUpdateFileRepository;

	@Override
	public boolean schemaAlreadyInitialized() {
		return commercialMedicationUpdateFileRepository.schemaAlreadyInitialized() != 0;
	}

	@Override
	public void saveNewEntry(Long logId) {
		commercialMedicationUpdateFileRepository.save(new CommercialMedicationUpdateFile(logId));
	}

	@Override
	public CommercialMedicationFileUpdateBo getLastNonProcessedEntry() {
		return commercialMedicationUpdateFileRepository.fetchLastNonProcessedEntry();
	}

	@Override
	public void setEntryAsProcessed(Integer id, Long lastLogId) {
		commercialMedicationUpdateFileRepository.setEntryAsProcessed(id);
		commercialMedicationUpdateFileRepository.save(new CommercialMedicationUpdateFile(lastLogId));
	}

}
