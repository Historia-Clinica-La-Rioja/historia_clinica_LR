package net.pladema.terminology.cache.controller.dto;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapperImpl;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;

@Builder
@AllArgsConstructor
public class TerminologyQueueItemDto {
	public final Integer id;
	public final SnomedECL ecl;
	public final String url;
	public final DateTimeDto createdOn;
	public final DateTimeDto downloadedOn;
	public final String downloadedError;
	public final boolean downloadedFile;
	public final DateTimeDto ingestedOn;
	public final String ingestedError;

	private static LocalDateMapper LOCAL_DATE_MAPPER = new LocalDateMapperImpl();

	public static TerminologyQueueItemDto fromEntity(SnomedCacheFile snomedCacheFile) {
		return TerminologyQueueItemDto.builder()
				.id(snomedCacheFile.getId())
				.ecl(SnomedECL.map(snomedCacheFile.getEcl()))
				.url(snomedCacheFile.getUrl())
				.createdOn(LOCAL_DATE_MAPPER.toDateTimeDto(snomedCacheFile.getCreatedOn()))
				.downloadedOn(LOCAL_DATE_MAPPER.toDateTimeDto(snomedCacheFile.getDownloadedOn()))
				.downloadedError(snomedCacheFile.getDownloadedError())
				.downloadedFile(snomedCacheFile.getFileId() != null)
				.ingestedOn(LOCAL_DATE_MAPPER.toDateTimeDto(snomedCacheFile.getIngestedOn()))
				.ingestedError(snomedCacheFile.getIngestedError())
				.build();
	}
}
