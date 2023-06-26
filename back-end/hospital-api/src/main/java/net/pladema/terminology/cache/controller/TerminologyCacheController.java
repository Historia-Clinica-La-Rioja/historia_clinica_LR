package net.pladema.terminology.cache.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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
import net.pladema.terminology.cache.controller.dto.TerminologyCSVDto;
import net.pladema.terminology.cache.controller.dto.TerminologyQueueItemDto;
import net.pladema.terminology.cache.infrastructure.output.repository.SnomedCacheFileRepository;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;


@AllArgsConstructor
@RestController
@RequestMapping("terminology/cache")
public class TerminologyCacheController {
	private final SnomedCacheFileRepository snomedCacheFileRepository;

	@PostMapping
	public @ResponseBody TerminologyQueueItemDto addCSV(
			@RequestBody TerminologyCSVDto terminologyCSVDto
	) {

		var newItem = entity(terminologyCSVDto);
		var saved = snomedCacheFileRepository.save(newItem);

		return TerminologyQueueItemDto.fromEntity(saved);
	}

	@GetMapping
	public @ResponseBody List<TerminologyQueueItemDto> getQueue() {
		var pendingItems = snomedCacheFileRepository.findToProcessByAge();
		return pendingItems.stream().map(
				TerminologyQueueItemDto::fromEntity
		).collect(Collectors.toList());
	}

	@DeleteMapping("/{terminologyId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable("terminologyId") Integer terminologyId) {
		snomedCacheFileRepository.delete(terminologyId);
	}

	private SnomedCacheFile entity(TerminologyCSVDto terminologyCSVDto) {
		return new SnomedCacheFile(
				terminologyCSVDto.ecl.toString(),
				terminologyCSVDto.url
		);
	}

}
