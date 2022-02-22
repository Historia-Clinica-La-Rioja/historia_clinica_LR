package net.pladema.snowstorm.services.loadCsv;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedCacheLogRepository;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedCacheLog;
import net.pladema.snowstorm.repository.entity.SnomedGroup;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateSnomedConceptsByCsv {

    private static final int BATCH_MAX_SIZE = 10000;
	private static final int BATCH_SIZE_DIVIDER = 10;
	private static final int BATCH_SIZE_MULTIPLIER = 10;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
    private final SnomedSemantics snomedSemantics;
    private final SnomedGroupRepository snomedGroupRepository;
    private final DateTimeProvider dateTimeProvider;
    private final SharedSnomedPort sharedSnomedPort;
	private final SnomedCacheLogRepository snomedCacheLogRepository;

    public UpdateConceptsResultBo run(MultipartFile csvFile, String eclKey) {
		log.debug("Input parameters -> csvFile {}, eclKey {}", csvFile.getOriginalFilename(), eclKey);

        Integer conceptsProcessed = 0;
		Integer erroneousConcepts = 0;
		List<String> errorMessages = new ArrayList<>();
		Integer batchSize = BATCH_MAX_SIZE;
        Integer totalConcepts = SnomedConceptsCsvReader.getTotalRecords(csvFile);
        LocalDate today = dateTimeProvider.nowDate();
        Integer snomedGroupId = saveSnomedGroup(eclKey, today);
        List<SnomedConceptBo> allConcepts = getAllConcepts(csvFile);
		List<SnomedConceptBo> conceptBatch = null;

        while (conceptsProcessed < totalConcepts) {
			try {
				conceptBatch = getNextBatch(batchSize, conceptsProcessed, totalConcepts, allConcepts);
				conceptsProcessed = saveConcepts(conceptsProcessed, today, snomedGroupId, conceptBatch);
				// if the batch size had decreased before due to an error, it will increase again
				batchSize = Math.min(batchSize * BATCH_SIZE_MULTIPLIER, BATCH_MAX_SIZE);
				log.debug("Concepts processed -> {}", conceptsProcessed);
			} catch (Exception e) {
				// If the batch size is equal to 1, it means that that element is the one that can't be saved.
				// So, it should be skipped to try to save the rest
				if (batchSize.equals(1)) {
					saveError(e, conceptBatch.get(0), errorMessages);
					conceptsProcessed += 1;
					erroneousConcepts += 1;
					batchSize = BATCH_MAX_SIZE;
				} else {
					batchSize = Math.max(batchSize / BATCH_SIZE_DIVIDER, 1);
				}
			}

        }
		UpdateConceptsResultBo result = new UpdateConceptsResultBo(eclKey,
				conceptsProcessed - erroneousConcepts,
				erroneousConcepts,
				errorMessages);
		log.debug("Finished loading snomed concepts");
		log.debug("Output -> {}", result);
		return result;
    }

	private void saveError(Exception e, SnomedConceptBo snomedConceptBo, List<String> errorMessages) {
		String message = String.format("Error saving %s -> %s", snomedConceptBo.toString(), e.getCause().getCause().getMessage());
		errorMessages.add(message);
		snomedCacheLogRepository.save(new SnomedCacheLog(message, dateTimeProvider.nowDateTime()));
	}

	private Integer saveConcepts(Integer conceptsProcessed, LocalDate today, Integer snomedGroupId, List<SnomedConceptBo> conceptBatch) {
		List<Integer> conceptIds = sharedSnomedPort.addSnomedConcepts(mapToDto(conceptBatch));
		conceptsProcessed = associateConceptIdsWithSnomedGroup(snomedGroupId, conceptIds, conceptsProcessed, today);
		return conceptsProcessed;
	}

	private List<SnomedConceptBo> getNextBatch(Integer batchSize, Integer conceptsProcessed, Integer totalConcepts, List<SnomedConceptBo> allConcepts) {
		int batchFinishIndex = Math.min(conceptsProcessed + batchSize, totalConcepts);
		return allConcepts.subList(conceptsProcessed, batchFinishIndex);
	}

	private List<SharedSnomedDto> mapToDto(List<SnomedConceptBo> concepts) {
        return concepts.stream()
                .map(c -> new SharedSnomedDto(c.getSctid(), c.getPt(), null, null))
                .collect(Collectors.toList());
    }

    private List<SnomedConceptBo> getAllConcepts(MultipartFile csvFile) {
		log.debug("Input parameter -> csvFile {}", csvFile.getOriginalFilename());
        List<SnomedConceptBo> result = new ArrayList<>();
        if (SnomedConceptsCsvReader.hasCsvFormat(csvFile)) {
            try {
                result = SnomedConceptsCsvReader.csvToSnomedConceptsBo(csvFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		log.debug("Output size -> {}", result.size());
		return result;
    }

    private Integer associateConceptIdsWithSnomedGroup(Integer snomedGroupId, List<Integer> conceptIds, Integer orden, LocalDate date) {
		log.debug("Input parameters -> snomedGroupId {}, conceptIds size = {}, orden {}, date {}",
				snomedGroupId, conceptIds.size(), orden, date);
        List<SnomedRelatedGroup> elementsToSave = new ArrayList<>();
        for (Integer snomedId : conceptIds) {
            SnomedRelatedGroup snomedRelatedGroup = new SnomedRelatedGroup(snomedId, snomedGroupId, orden, date);
            elementsToSave.add(snomedRelatedGroup);
            orden = orden + 1;
        }
        snomedRelatedGroupRepository.saveAll(elementsToSave);
		log.debug("Output -> {}", orden);
        return orden;
    }

    private Integer saveSnomedGroup(String eclKey, LocalDate date) {
		log.debug("Input parameters -> eclKey {}, date {}", eclKey, date);
        String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
        Integer snomedGroupId = snomedGroupRepository.getIdByEcl(ecl);
        Integer customId = 1;
        SnomedGroup toSave = new SnomedGroup(snomedGroupId, eclKey, ecl, customId, date);
		Integer result = snomedGroupRepository.save(toSave).getId();
		log.debug("Output -> {}", result);
        return result;
    }

}
