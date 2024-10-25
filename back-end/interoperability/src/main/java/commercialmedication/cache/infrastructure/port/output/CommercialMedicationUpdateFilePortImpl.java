package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationUpdateFilePort;

import commercialmedication.cache.domain.CommercialMedicationFileUpdateBo;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationUpdateFileRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationUpdateFile;

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
