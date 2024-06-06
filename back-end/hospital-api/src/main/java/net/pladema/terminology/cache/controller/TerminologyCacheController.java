package net.pladema.terminology.cache.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.terminology.cache.controller.dto.ETerminologyKind;
import net.pladema.terminology.cache.controller.dto.TerminologyCSVDto;
import net.pladema.terminology.cache.controller.dto.TerminologyECLStatusDto;
import net.pladema.terminology.cache.controller.dto.TerminologyQueueItemDto;
import net.pladema.terminology.cache.infrastructure.output.repository.SnomedCacheFileRepository;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;


@AllArgsConstructor
@RestController
@RequestMapping("terminology/cache")
public class TerminologyCacheController {
	private final SnomedCacheFileRepository snomedCacheFileRepository;

	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@PostMapping
	public @ResponseBody TerminologyQueueItemDto addCSV(
			@RequestBody TerminologyCSVDto terminologyCSVDto
	) {

		var newItem = entity(terminologyCSVDto);
		var saved = snomedCacheFileRepository.save(newItem);

		return TerminologyQueueItemDto.fromEntity(saved);
	}

	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@GetMapping("/{kind}")
	public @ResponseBody List<TerminologyQueueItemDto> getQueue(
			@PathVariable(name = "kind") ETerminologyKind kind
	) {
		var pendingItems = snomedCacheFileRepository.findToProcessByAge(kind);
		return pendingItems.stream().map(
				TerminologyQueueItemDto::fromEntity
		).collect(Collectors.toList());
	}

	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@GetMapping("/{kind}/status")
	public List<TerminologyECLStatusDto> getStatus(
			@PathVariable(name = "kind") ETerminologyKind kind
	) {
		var eclStatusList = snomedCacheFileRepository.lastSuccessfulByECL(kind);

		return Arrays.stream(SnomedECL.values())
				.sorted()
				.map(ecl -> findEclStatusInList(ecl, eclStatusList))
				.collect(Collectors.toList());

	}

	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@DeleteMapping("/{terminologyId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable("terminologyId") Integer terminologyId) {
		snomedCacheFileRepository.delete(terminologyId);
	}

	private static TerminologyECLStatusDto findEclStatusInList(SnomedECL ecl, List<SnomedCacheFile> eclStatusList) {
		var successful = eclStatusList.stream().filter(
				s -> s.getEcl().equals(ecl.toString())
		).findFirst().orElse(null);

		if (successful == null) {
			return new TerminologyECLStatusDto(ecl, null);
		}

		return new TerminologyECLStatusDto(
				ecl,
				TerminologyQueueItemDto.fromEntity(successful)
		);
	}

	private static SnomedCacheFile entity(TerminologyCSVDto terminologyCSVDto) {
		return new SnomedCacheFile(
				terminologyCSVDto.ecl.toString(),
				terminologyCSVDto.url,
				terminologyCSVDto.kind
		);
	}


}
