package net.pladema.terminology.cache.controller.dto;

import lombok.AllArgsConstructor;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

@AllArgsConstructor
public class TerminologyECLStatusDto {
	public final SnomedECL ecl;
	public final TerminologyQueueItemDto successful;
}
