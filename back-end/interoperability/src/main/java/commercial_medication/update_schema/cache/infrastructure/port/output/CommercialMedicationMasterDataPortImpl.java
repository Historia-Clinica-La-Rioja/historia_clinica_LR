package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;

import commercial_medication.update_schema.cache.application.port.CommercialMedicationMasterDataPort;
import commercial_medication.update_schema.cache.domain.decodedResponse.DatabaseUpdate;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationActionRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationControlRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationFormRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationLaboratoryRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationMonoDrugRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationNewDrugRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationPotencyRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationQuantityRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationSellTypeRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationSizeRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationViaRepository;
import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationAction;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationControl;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationForm;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationLaboratory;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationMonoDrug;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationNewDrug;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationPotency;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationQuantity;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationSellType;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationSize;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationVia;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommercialMedicationMasterDataPortImpl implements CommercialMedicationMasterDataPort {

	private final String ACTION_TABLE = "Acciones";

	private final String CONTROL_TABLE = "Control";

	private final String FORM_TABLE = "Formas";

	private final String LABORATORY_TABLE = "Laborato";

	private final String MONO_DRUG_TABLE = "Monodro";

	private final String NEW_DRUG_TABLE = "NuevaDro";

	private final String POTENCY_TABLE = "Potencia";

	private final String QUANTITY_TABLE = "Cantidad";

	private final String SELL_TYPE_TABLE = "TipoVen";

	private final String SIZE_TABLE = "Tamanio";

	private final String VIA_TABLE = "Via";

	private final CommercialMedicationActionRepository commercialMedicationActionRepository;

	private final CommercialMedicationControlRepository commercialMedicationControlRepository;

	private final CommercialMedicationFormRepository commercialMedicationFormRepository;

	private final CommercialMedicationLaboratoryRepository commercialMedicationLaboratoryRepository;

	private final CommercialMedicationMonoDrugRepository commercialMedicationMonoDrugRepository;

	private final CommercialMedicationNewDrugRepository commercialMedicationNewDrugRepository;

	private final CommercialMedicationPotencyRepository commercialMedicationPotencyRepository;

	private final CommercialMedicationQuantityRepository commercialMedicationQuantityRepository;

	private final CommercialMedicationSellTypeRepository commercialMedicationSellTypeRepository;

	private final CommercialMedicationSizeRepository commercialMedicationSizeRepository;

	private final CommercialMedicationViaRepository commercialMedicationViaRepository;

	@Override
	public void saveNewMasterDataFromUpdate(List<DatabaseUpdate> databaseUpdates) {
		Map<String, List<DatabaseUpdate>> classifiedMasterDataUpdates = classifyMasterDataUpdates(databaseUpdates);
		if (classifiedMasterDataUpdates.get(ACTION_TABLE) != null)
			commercialMedicationActionRepository.saveAll(parseToActionEntityList(classifiedMasterDataUpdates.get(ACTION_TABLE)));
		if (classifiedMasterDataUpdates.get(CONTROL_TABLE) != null)
			commercialMedicationControlRepository.saveAll(parseToControlEntityList(classifiedMasterDataUpdates.get(CONTROL_TABLE)));
		if (classifiedMasterDataUpdates.get(FORM_TABLE) != null)
			commercialMedicationFormRepository.saveAll(parseToFormEntityList(classifiedMasterDataUpdates.get(FORM_TABLE)));
		if (classifiedMasterDataUpdates.get(LABORATORY_TABLE) != null)
			commercialMedicationLaboratoryRepository.saveAll(parseToLaboratoryEntityList(classifiedMasterDataUpdates.get(LABORATORY_TABLE)));
		if (classifiedMasterDataUpdates.get(MONO_DRUG_TABLE) != null)
			commercialMedicationMonoDrugRepository.saveAll(parseToMonoDrugEntityList(classifiedMasterDataUpdates.get(MONO_DRUG_TABLE)));
		if (classifiedMasterDataUpdates.get(NEW_DRUG_TABLE) != null)
			commercialMedicationNewDrugRepository.saveAll(parseToNewDrugEntityList(classifiedMasterDataUpdates.get(NEW_DRUG_TABLE)));
		if (classifiedMasterDataUpdates.get(POTENCY_TABLE) != null)
			commercialMedicationPotencyRepository.saveAll(parseToPotencyEntityList(classifiedMasterDataUpdates.get(POTENCY_TABLE)));
		if (classifiedMasterDataUpdates.get(QUANTITY_TABLE) != null)
			commercialMedicationQuantityRepository.saveAll(parseToQuantityEntityList(classifiedMasterDataUpdates.get(QUANTITY_TABLE)));
		if (classifiedMasterDataUpdates.get(SELL_TYPE_TABLE) != null)
			commercialMedicationSellTypeRepository.saveAll(parseToSellTypeEntityList(classifiedMasterDataUpdates.get(SELL_TYPE_TABLE)));
		if (classifiedMasterDataUpdates.get(SIZE_TABLE) != null)
			commercialMedicationSizeRepository.saveAll(parseToSizeEntityList(classifiedMasterDataUpdates.get(SIZE_TABLE)));
		if (classifiedMasterDataUpdates.get(VIA_TABLE) != null)
			commercialMedicationViaRepository.saveAll(parseToViaEntityList(classifiedMasterDataUpdates.get(VIA_TABLE)));
	}

	private List<CommercialMedicationVia> parseToViaEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationVia(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationSize> parseToSizeEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationSize(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationSellType> parseToSellTypeEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationSellType(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationQuantity> parseToQuantityEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationQuantity(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationPotency> parseToPotencyEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationPotency(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationNewDrug> parseToNewDrugEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationNewDrug(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationMonoDrug> parseToMonoDrugEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationMonoDrug(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationLaboratory> parseToLaboratoryEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationLaboratory(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationForm> parseToFormEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationForm(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationControl> parseToControlEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationControl(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	private List<CommercialMedicationAction> parseToActionEntityList(List<DatabaseUpdate> databaseUpdates) {
		return databaseUpdates.stream()
				.map(update -> new CommercialMedicationAction(update.getAffectedRecordId(), update.getNewEntryDescription()))
				.collect(Collectors.toList());
	}

	@Override
	public void editMasterData(List<DatabaseUpdate> databaseUpdates) {
		Map<String, List<DatabaseUpdate>> classifiedUpdates = classifyMasterDataUpdates(databaseUpdates);
		if (classifiedUpdates.get(ACTION_TABLE) != null)
			classifiedUpdates.get(ACTION_TABLE).forEach(update -> {
				if (commercialMedicationActionRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationActionRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(CONTROL_TABLE) != null)
			classifiedUpdates.get(CONTROL_TABLE).forEach(update -> {
				if (commercialMedicationControlRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationControlRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(FORM_TABLE) != null)
			classifiedUpdates.get(FORM_TABLE).forEach(update -> {
				if (commercialMedicationFormRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationFormRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(LABORATORY_TABLE) != null)
			classifiedUpdates.get(LABORATORY_TABLE).forEach(update -> {
				if (commercialMedicationLaboratoryRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationLaboratoryRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(MONO_DRUG_TABLE) != null)
			classifiedUpdates.get(MONO_DRUG_TABLE).forEach(update -> {
				if (commercialMedicationMonoDrugRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationMonoDrugRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(NEW_DRUG_TABLE) != null)
			classifiedUpdates.get(NEW_DRUG_TABLE).forEach(update -> {
				if (commercialMedicationNewDrugRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationNewDrugRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(POTENCY_TABLE) != null)
			classifiedUpdates.get(POTENCY_TABLE).forEach(update -> {
				if (commercialMedicationPotencyRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationPotencyRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(QUANTITY_TABLE) != null)
			classifiedUpdates.get(QUANTITY_TABLE).forEach(update -> {
				if (commercialMedicationQuantityRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationQuantityRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(SELL_TYPE_TABLE) != null)
			classifiedUpdates.get(SELL_TYPE_TABLE).forEach(update -> {
				if (commercialMedicationSellTypeRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationSellTypeRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(SIZE_TABLE) != null)
			classifiedUpdates.get(SIZE_TABLE).forEach(update -> {
				if (commercialMedicationSizeRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationSizeRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
		if (classifiedUpdates.get(VIA_TABLE) != null)
			classifiedUpdates.get(VIA_TABLE).forEach(update -> {
				if (commercialMedicationViaRepository.existsById(update.getAffectedRecordId()))
					commercialMedicationViaRepository.updateDescription(update.getAffectedRecordId(), update.getNewEntryDescription());
			});
	}

	@Override
	public void deleteMasterData(List<DatabaseUpdate> databaseUpdates) {
		Map<String, List<Integer>> classifiedMasterDataUpdateIds = classifyMasterDataUpdateIds(databaseUpdates);
		if (classifiedMasterDataUpdateIds.get(ACTION_TABLE) != null)
			commercialMedicationActionRepository.deleteAllById(classifiedMasterDataUpdateIds.get(ACTION_TABLE));
		if (classifiedMasterDataUpdateIds.get(CONTROL_TABLE) != null)
			commercialMedicationControlRepository.deleteAllById(classifiedMasterDataUpdateIds.get(CONTROL_TABLE));
		if (classifiedMasterDataUpdateIds.get(FORM_TABLE) != null)
			commercialMedicationFormRepository.deleteAllById(classifiedMasterDataUpdateIds.get(FORM_TABLE));
		if (classifiedMasterDataUpdateIds.get(LABORATORY_TABLE) != null)
			commercialMedicationLaboratoryRepository.deleteAllById(classifiedMasterDataUpdateIds.get(LABORATORY_TABLE));
		if (classifiedMasterDataUpdateIds.get(MONO_DRUG_TABLE) != null)
			commercialMedicationMonoDrugRepository.deleteAllById(classifiedMasterDataUpdateIds.get(MONO_DRUG_TABLE));
		if (classifiedMasterDataUpdateIds.get(NEW_DRUG_TABLE) != null)
			commercialMedicationNewDrugRepository.deleteAllById(classifiedMasterDataUpdateIds.get(NEW_DRUG_TABLE));
		if (classifiedMasterDataUpdateIds.get(POTENCY_TABLE) != null)
			commercialMedicationPotencyRepository.deleteAllById(classifiedMasterDataUpdateIds.get(POTENCY_TABLE));
		if (classifiedMasterDataUpdateIds.get(QUANTITY_TABLE) != null)
			commercialMedicationQuantityRepository.deleteAllById(classifiedMasterDataUpdateIds.get(QUANTITY_TABLE));
		if (classifiedMasterDataUpdateIds.get(SELL_TYPE_TABLE) != null)
			commercialMedicationSellTypeRepository.deleteAllById(classifiedMasterDataUpdateIds.get(SELL_TYPE_TABLE));
		if (classifiedMasterDataUpdateIds.get(SIZE_TABLE) != null)
			commercialMedicationSizeRepository.deleteAllById(classifiedMasterDataUpdateIds.get(SIZE_TABLE));
		if (classifiedMasterDataUpdateIds.get(VIA_TABLE) != null)
			commercialMedicationViaRepository.deleteAllById(classifiedMasterDataUpdateIds.get(VIA_TABLE));
	}

	private Map<String, List<Integer>> classifyMasterDataUpdateIds(List<DatabaseUpdate> databaseUpdates) {
		Map<String, List<Integer>> result = new HashMap<>();
		databaseUpdates.forEach(update -> result.computeIfAbsent(update.getTableName(), k -> new ArrayList<>()).add(update.getAffectedRecordId()));
		return result;
	}

	private Map<String, List<DatabaseUpdate>> classifyMasterDataUpdates(List<DatabaseUpdate> databaseUpdates) {
		Map<String, List<DatabaseUpdate>> result = new HashMap<>();
		databaseUpdates.forEach(update -> result.computeIfAbsent(update.getTableName(), k -> new ArrayList<>()).add(update));
		return result;
	}

}
