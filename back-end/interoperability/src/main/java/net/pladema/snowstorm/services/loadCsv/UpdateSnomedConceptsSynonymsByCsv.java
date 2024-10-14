package net.pladema.snowstorm.services.loadCsv;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.pladema.snowstorm.services.loadCsv.exceptions.EUpdateSnomedConceptsException;
import net.pladema.snowstorm.services.loadCsv.exceptions.UpdateSnomedConceptsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedCacheLogRepository;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedCacheLog;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateSnomedConceptsSynonymsByCsv {

	@Value("${snomed-cache-update.batch-size:1000}")
	private int batchMaxSize;
	private static final int BATCH_SIZE_DIVIDER = 10;
	private static final int BATCH_SIZE_MULTIPLIER = 10;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final SnomedSemantics snomedSemantics;
	private final SnomedGroupRepository snomedGroupRepository;
	private final DateTimeProvider dateTimeProvider;
	private final SharedSnomedPort sharedSnomedPort;
	private final SnomedCacheLogRepository snomedCacheLogRepository;

	public UpdateConceptsResultBo updateSnomedConceptSynonyms(
			InputStreamSource csvFile,
			String eclKey
	){
		Integer conceptsProcessed = 0;
		Integer erroneousConcepts = 0;
		Integer missingMainConcepts = 0;
		List<String> errorMessages = new ArrayList<>();
		Integer batchSize = batchMaxSize;
		Integer totalConcepts = SnomedConceptsCsvReader.getTotalRecords(csvFile);
		LocalDate today = dateTimeProvider.nowDate();
		Integer snomedGroupId = getSnomedGroupId(eclKey, today);
		List<SnomedConceptBo> conceptBatch = null;

		log.debug("Total concepts to process -> {}", totalConcepts);
		while (conceptsProcessed < totalConcepts) {
			try {
				conceptBatch = getNextBatch(batchSize, conceptsProcessed, totalConcepts, csvFile);
				conceptsProcessed = saveConcepts(conceptsProcessed, today, snomedGroupId, conceptBatch, missingMainConcepts);
				// if the batch size had decreased before due to an error, it will increase again
				batchSize = Math.min(batchSize * BATCH_SIZE_MULTIPLIER, batchMaxSize);
				log.debug("Concepts processed -> {}", conceptsProcessed);
			} catch (Throwable e) {
				// If the batch size is equal to 1, it means that that element is the one that can't be saved.
				// So, it should be skipped to try to save the rest
				if (batchSize.equals(1)) {
					log.error("Capturing Throwable Update Snomed -> {}", e.getMessage());
					saveError(e, conceptBatch.get(0), errorMessages);
					conceptsProcessed += 1;
					erroneousConcepts += 1;
					batchSize = batchMaxSize;
				} else {
					batchSize = Math.max(batchSize / BATCH_SIZE_DIVIDER, 1);
				}
			}

		}
		UpdateConceptsResultBo result = new UpdateConceptsResultBo(
				conceptsProcessed - erroneousConcepts,
				erroneousConcepts,
				errorMessages,
				missingMainConcepts
		);
		log.debug("Finished loading snomed concepts");
		log.debug("Output -> {}", result);
		return result;
	}

	private void saveError(Throwable e, SnomedConceptBo snomedConceptBo, List<String> errorMessages) {
		String particularError = e.getCause() != null
				? e.getCause().getMessage()
				: e.getMessage();
		String message = String.format("Error saving %s -> %s", snomedConceptBo.toString(), particularError);
		errorMessages.add(message);
		snomedCacheLogRepository.save(new SnomedCacheLog(message, dateTimeProvider.nowDateTime()));
	}

	private List<SnomedConceptBo> getNextBatch(Integer batchSize, Integer conceptsProcessed, Integer totalConcepts, InputStreamSource csvFile) {
		int batchFinishIndex = Math.min(conceptsProcessed + batchSize, totalConcepts);
		return getConcepts(csvFile, conceptsProcessed, batchFinishIndex);
	}

	private List<SnomedConceptBo> getConcepts(InputStreamSource csvFile, int start, int end) {
		log.debug("Input parameter -> getConcepts start {}, end {}", start, end);
		List<SnomedConceptBo> result = new ArrayList<>();
		try {
			result = SnomedConceptsCsvReader.csvToSnomedConceptsBo(csvFile.getInputStream(), start, end);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		log.debug("Output size -> {}", result.size());
		return result;
	}

	private Integer getSnomedGroupId(String eclKey, LocalDate date) {
		log.debug("Input parameters -> eclKey {}, date {}", eclKey, date);
		String ecl = "";
		try {
			ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
		} catch (NotFoundException e){
			throw e;
		}
		Integer result = snomedGroupRepository.getBaseGroupIdByEclAndDescription(ecl, eclKey);
		log.debug("Output -> {}", result);
		return result;
	}

	private Integer saveConcepts(Integer conceptsProcessed, LocalDate today, Integer snomedGroupId, List<SnomedConceptBo> conceptBatch, Integer missingMainConcepts) {
		List<Integer> conceptIds = sharedSnomedPort.addSnomedSynonyms(mapToDto(conceptBatch));
		if (conceptIds.isEmpty()) {
			throw new UpdateSnomedConceptsException(EUpdateSnomedConceptsException.NO_SYNONYMS_BATCH_WERE_CREATED,
					"No se han creado nuevos sinónimos, ya que no se encuentran definidos en snomed.");
		}
		conceptsProcessed = associateConceptIdsWithSnomedGroup(snomedGroupId, conceptIds, conceptsProcessed, today);
		missingMainConcepts = missingMainConcepts + (conceptBatch.size() - conceptsProcessed);
		return conceptsProcessed;
	}

	private List<SharedSnomedDto> mapToDto(List<SnomedConceptBo> concepts) {
		return concepts.stream()
				.map(c -> new SharedSnomedDto(c.getSctid(), c.getPt(), null, null))
				.collect(Collectors.toList());
	}

	private Integer associateConceptIdsWithSnomedGroup(Integer snomedGroupId, List<Integer> conceptIds, Integer orden, LocalDate date) {
		log.debug("Input parameters -> snomedGroupId {}, conceptIds size = {}, orden {}, date {}",
				snomedGroupId, conceptIds.size(), orden, date);
		List<SnomedRelatedGroup> elementsToSave = new ArrayList<>();
		for (Integer snomedId : conceptIds) {
			SnomedRelatedGroup snomedRelatedGroup = snomedRelatedGroupRepository.getByGroupIdAndSnomedId(snomedGroupId, snomedId)
					.orElse(new SnomedRelatedGroup(snomedId, snomedGroupId));
			snomedRelatedGroup.setLastUpdate(date);
			snomedRelatedGroup.setOrden(orden);
			elementsToSave.add(snomedRelatedGroup);
			orden = orden + 1;
		}
		snomedRelatedGroupRepository.saveAll(elementsToSave);
		log.debug("Output -> {}", orden);
		return orden;
	}

}
