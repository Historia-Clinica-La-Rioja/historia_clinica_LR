package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationUpdateLogPort;

import commercialmedication.cache.domain.CommercialMedicationUpdateLogBo;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationUpdateLogRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationUpdateLog;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommercialMedicationUpdateLogPortImpl implements CommercialMedicationUpdateLogPort {

	private final CommercialMedicationUpdateLogRepository commercialMedicationUpdateLogRepository;

	@Override
	public boolean schemaAlreadyInitialized() {
		return commercialMedicationUpdateLogRepository.schemaAlreadyInitialized() != 0;
	}

	@Override
	public void saveNewEntry(Long logId) {
		commercialMedicationUpdateLogRepository.save(new CommercialMedicationUpdateLog(logId));
	}

	@Override
	public CommercialMedicationUpdateLogBo getLastNonProcessedEntry() {
		return commercialMedicationUpdateLogRepository.fetchLastNonProcessedEntry();
	}

	@Override
	public void setEntryAsProcessed(Integer id, Long lastLogId) {
		commercialMedicationUpdateLogRepository.setEntryAsProcessed(id);
		commercialMedicationUpdateLogRepository.save(new CommercialMedicationUpdateLog(lastLogId));
	}

}
