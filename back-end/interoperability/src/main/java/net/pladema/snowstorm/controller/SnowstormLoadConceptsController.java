package net.pladema.snowstorm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.controller.dto.UpdateConceptsResultDto;
import net.pladema.snowstorm.controller.dto.UpdateConceptsSynonymsResultDto;
import net.pladema.snowstorm.services.loadCsv.UpdateConceptsResultBo;
import net.pladema.snowstorm.services.loadCsv.UpdateConceptsSynonymsResultBo;
import net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv;
import net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsSynonymsByCsv;

@Slf4j
@RestController
@RequestMapping("/snowstorm")
@Tag(name = "Snowstorm", description = "Snowstorm")
public class SnowstormLoadConceptsController {

    private final UpdateSnomedConceptsByCsv updateSnomedConceptsByCsv;

	private final UpdateSnomedConceptsSynonymsByCsv updateSnomedConceptsSynonymsByCsv;

    public SnowstormLoadConceptsController(
	   UpdateSnomedConceptsByCsv updateSnomedConceptsByCsv,
	   UpdateSnomedConceptsSynonymsByCsv updateSnomedConceptsSynonymsByCsv
	) {
        this.updateSnomedConceptsByCsv = updateSnomedConceptsByCsv;
		this.updateSnomedConceptsSynonymsByCsv = updateSnomedConceptsSynonymsByCsv;
	}


    @PostMapping("/load-concepts-csv")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public UpdateConceptsResultDto loadConceptsByCsv(@RequestParam("file") MultipartFile file,
						   @RequestParam(value = "ecl") String eclKey) {
        log.debug("Input parameters -> file {}, eclKey {}", file.getOriginalFilename(), eclKey);
		UpdateConceptsResultDto result = mapToUpdateConceptsResultDto(updateSnomedConceptsByCsv.run(file, eclKey));
		log.debug("Output -> {}", result);
		return result;
    }

	@PostMapping("/load-concepts-synonyms-csv")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public UpdateConceptsSynonymsResultDto loadConceptsSynonymsByCsv(@RequestParam("file") MultipartFile file,
													 @RequestParam(value = "ecl") String eclKey) {
		log.debug("Input parameters -> file {}, eclKey {}", file.getOriginalFilename(), eclKey);
		UpdateConceptsSynonymsResultDto result = mapToUpdateConceptsSynonymsResultDto(updateSnomedConceptsSynonymsByCsv.run(file, eclKey));
		log.debug("Output -> {}", result);
		return result;
	}

	private UpdateConceptsResultDto mapToUpdateConceptsResultDto(UpdateConceptsResultBo updateConceptsResultBo) {
		return new UpdateConceptsResultDto(
				updateConceptsResultBo.getEclKey(),
				updateConceptsResultBo.getConceptsLoaded(),
				updateConceptsResultBo.getErroneousConcepts(),
				updateConceptsResultBo.getErrorMessages());
	}

	private UpdateConceptsSynonymsResultDto mapToUpdateConceptsSynonymsResultDto(UpdateConceptsSynonymsResultBo updateConceptsSynonymsResultBo) {
		return new UpdateConceptsSynonymsResultDto(
				updateConceptsSynonymsResultBo.getEclKey(),
				updateConceptsSynonymsResultBo.getConceptsLoaded(),
				updateConceptsSynonymsResultBo.getErroneousConcepts(),
				updateConceptsSynonymsResultBo.getMissingMainConcepts(),
				updateConceptsSynonymsResultBo.getErrorMessages());
	}


}
