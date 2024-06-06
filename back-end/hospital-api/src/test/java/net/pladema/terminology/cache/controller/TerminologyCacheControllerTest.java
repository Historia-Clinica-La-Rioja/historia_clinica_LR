package net.pladema.terminology.cache.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import net.pladema.UnitRepository;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.loadCsv.UpdateConceptsResultBo;
import net.pladema.terminology.cache.controller.dto.ETerminologyKind;
import net.pladema.terminology.cache.controller.dto.TerminologyCSVDto;
import net.pladema.terminology.cache.controller.dto.TerminologyECLStatusDto;
import net.pladema.terminology.cache.controller.dto.TerminologyQueueItemDto;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileIngestor;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileStorage;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileDownloadService;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileIngestService;
import net.pladema.terminology.cache.infrastructure.output.repository.SnomedCacheFileRepository;
import net.pladema.terminology.cache.jobs.DownloadCacheCSVJob;
import net.pladema.terminology.cache.jobs.IngestCacheCSVJob;

import org.springframework.boot.test.mock.mockito.MockBean;


@ExtendWith(MockitoExtension.class)
class TerminologyCacheControllerTest extends UnitRepository {
	private static final TerminologyCSVDto ALLERGY_CSV = new TerminologyCSVDto(SnomedECL.ALLERGY, "http://lamansys.ar/ecl/ALLERGY_SYN.csv", ETerminologyKind.SYNONYM);

	private static final TerminologyCSVDto ALLERGY_TERM_CSV = new TerminologyCSVDto(SnomedECL.ALLERGY, "http://lamansys.ar/ecl/ALLERGY_TERM.csv", ETerminologyKind.TERMINOLOGY);
	private static final TerminologyCSVDto DIAGNOSIS_CSV = new TerminologyCSVDto(SnomedECL.DIAGNOSIS, "http://lamansys.ar/ecl/ECL.csv", ETerminologyKind.TERMINOLOGY);
	private static final TerminologyCSVDto PROCEDURE_CSV = new TerminologyCSVDto(SnomedECL.PROCEDURE, "http://lamansys.ar/ecl/PROCEDURE.csv", ETerminologyKind.TERMINOLOGY);
	private static final TerminologyCSVDto BAD_CSV = new TerminologyCSVDto(SnomedECL.FAMILY_RECORD, "http://lamansys.ar/ecl/picture.png", ETerminologyKind.TERMINOLOGY);


	@Autowired
	private SnomedCacheFileRepository snomedCacheFileRepository;

	@Mock
	private SnomedCacheFileStorage snomedCacheFileStorage;

	@Mock
	private SnomedCacheFileIngestor snomedCacheFileIngestor;

	private DownloadCacheCSVJob downloadJob;
	private IngestCacheCSVJob ingestJob;

	private TerminologyCacheController controller;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	void setUp() {
		controller = new TerminologyCacheController(snomedCacheFileRepository);
		downloadJob = new DownloadCacheCSVJob(
				new SnowmedCacheFileDownloadService(snomedCacheFileRepository),
				snomedCacheFileStorage
		);
		ingestJob = new IngestCacheCSVJob(
				new SnowmedCacheFileIngestService(snomedCacheFileRepository),
				snomedCacheFileIngestor
		);
	}

	@Test
	void addCSV_ToQueue() {

		controller.addCSV(DIAGNOSIS_CSV);

		var queueItem = assertThatTerminologyQueueHasSize(1).get(0);

		assertThat(queueItem)
				.hasFieldOrPropertyWithValue("url", "http://lamansys.ar/ecl/ECL.csv")
				.hasFieldOrPropertyWithValue("ecl", SnomedECL.DIAGNOSIS)
				.hasFieldOrPropertyWithValue("downloadedError", null)
		;

	}

	@Test
	void addCSV_Download_WithError() {

		controller.addCSV(ALLERGY_TERM_CSV);

		downloadFail(ALLERGY_TERM_CSV.url, "Imposible leer");

		var queueItem = assertThatTerminologyQueueHasSize(1).get(0);

		assertThat(queueItem)
				.hasFieldOrPropertyWithValue("url", "http://lamansys.ar/ecl/ALLERGY_TERM.csv")
				.hasFieldOrPropertyWithValue("ecl", SnomedECL.ALLERGY)
				.hasFieldOrPropertyWithValue("downloadedError", "Imposible leer")
		;
	}

	@Test
	void addCSV_Download_Ingest() {

		controller.addCSV(ALLERGY_CSV);

		assertThatQueueHasSize(1, 0);

		downloadJob(ALLERGY_CSV.url, 18L);

		assertThatQueueHasSize(1, 0);

		ingestJob(ALLERGY_CSV.ecl, 18L, ALLERGY_CSV.kind);

		assertThatQueueHasSize(0, 0);

	}

	@Test
	void addCSV_Download_Injest_Multiple() {

		controller.addCSV(ALLERGY_CSV);
		controller.addCSV(DIAGNOSIS_CSV);

		assertThatQueueHasSize(1, 1);

		downloadJob(ALLERGY_CSV.url, 18L);
		ingestJob(ALLERGY_CSV.ecl, 18L, ALLERGY_CSV.kind);

		assertThatQueueHasSize(0, 1);

		downloadJob(DIAGNOSIS_CSV.url, 19L);
		ingestJobFail(DIAGNOSIS_CSV.ecl, 19L, DIAGNOSIS_CSV.kind);

		ingestJob.execute();

		assertThatQueueHasSize(0, 0);

	}

	@Test
	void addCSV_getStatus() {

		assertThat(controller.getStatus(ETerminologyKind.TERMINOLOGY))
				.hasSize(SnomedECL.values().length);
		assertThat(controller.getStatus(ETerminologyKind.SYNONYM))
				.hasSize(SnomedECL.values().length);

		assertThatQueueHasSize(0, 0);
		controller.addCSV(ALLERGY_CSV);
		assertThatQueueHasSize(1, 0);
		controller.addCSV(DIAGNOSIS_CSV);
		assertThatQueueHasSize(1, 1);
		controller.addCSV(BAD_CSV);
		assertThatQueueHasSize(1, 2);

		downloadJob(ALLERGY_CSV.url, 13L); // allergy syn
		assertThatQueueHasSize(1, 2);

		downloadJob(DIAGNOSIS_CSV.url, 14L); // diagnosis
		assertThatQueueHasSize(1, 2);

		downloadJob(BAD_CSV.url, 15L); // bad csv
		assertThatQueueHasSize(1, 2);

		ingestJob(ALLERGY_CSV.ecl, 13L, ALLERGY_CSV.kind);
		assertThatQueueHasSize(0, 2);
		ingestJob(DIAGNOSIS_CSV.ecl, 14L, DIAGNOSIS_CSV.kind);
		assertThatQueueHasSize(0, 1);
		ingestJobFail(BAD_CSV.ecl, 15L, BAD_CSV.kind); // bad csv
		assertThatQueueHasSize(0, 0);

		controller.addCSV(ALLERGY_CSV);
		assertThatQueueHasSize(1, 0);

		controller.addCSV(PROCEDURE_CSV);
		assertThatQueueHasSize(1, 1);


		var statusFound = controller.getStatus(ETerminologyKind.TERMINOLOGY);

		assertThat(getEclStatus(statusFound, SnomedECL.ALLERGY).successful)
				.isNull();
		assertThat(getEclStatus(statusFound, SnomedECL.DIAGNOSIS).successful)
				.hasFieldOrProperty("id");
		assertThat(getEclStatus(statusFound, BAD_CSV.ecl).successful)
				.isNull();
		assertThat(getEclStatus(statusFound, SnomedECL.PROCEDURE).successful)
				.isNull();

		var statusSynonimFound = controller.getStatus(ETerminologyKind.SYNONYM);

		assertThat(getEclStatus(statusSynonimFound, SnomedECL.ALLERGY).successful)
				.hasFieldOrProperty("id");
		assertThat(getEclStatus(statusSynonimFound, SnomedECL.DIAGNOSIS).successful)
				.isNull();
		assertThat(getEclStatus(statusSynonimFound, BAD_CSV.ecl).successful)
				.isNull();
		assertThat(getEclStatus(statusSynonimFound, SnomedECL.PROCEDURE).successful)
				.isNull();
	}

	private List<TerminologyQueueItemDto> assertThatTerminologyQueueHasSize(int expected) {
		var queue = controller.getQueue(ETerminologyKind.TERMINOLOGY);
		assertThat(queue).hasSize(expected);
		return queue;
	}

	private void assertThatQueueHasSize(int expectedSyn, int expectedTerm) {
		assertThat(controller.getQueue(ETerminologyKind.TERMINOLOGY)).hasSize(expectedTerm);
		assertThat(controller.getQueue(ETerminologyKind.SYNONYM)).hasSize(expectedSyn);
	}

	private void downloadJob(String url, Long fileId) {
		when(snomedCacheFileStorage.save(eq(url)))
				.thenReturn(newFileInfo(fileId));
		downloadJob.execute();
	}

	private void downloadFail(String url, String message) {
		when(snomedCacheFileStorage.save(eq(url)))
				.thenThrow(new RuntimeException(message));
		downloadJob.execute();
	}

	private void ingestJob(SnomedECL ecl, Long fileId, ETerminologyKind kind) {
		when(snomedCacheFileIngestor.run(eq(fileId), eq(ecl.name()), eq(kind)))
				.thenReturn(newUpdateConceptsResultBo());

		ingestJob.execute();
	}

	private void ingestJobFail(SnomedECL ecl, Long fileId, ETerminologyKind kind) {
		when(snomedCacheFileIngestor.run(eq(fileId), eq(ecl.name()), eq(kind)))
				.thenThrow(new RuntimeException("Failed to parse CSV file: (line 28) invalid char between encapsulated token and delimiter"));
		ingestJob.execute();
	}

	private static TerminologyECLStatusDto getEclStatus(
			List<TerminologyECLStatusDto> statusFound,
			SnomedECL snomedECL
	) {
		return statusFound.stream()
				.filter(isStatusForECL(snomedECL))
				.findFirst()
				.get();
	}

	private static Predicate<TerminologyECLStatusDto> isStatusForECL(SnomedECL snomedECL) {
		return (s) -> s.ecl.equals(snomedECL);
	}

	private static FileInfo newFileInfo(Long id) {
		var fileInfo = new FileInfo();
		fileInfo.setId(id);
		return fileInfo;
	}

	private static UpdateConceptsResultBo newUpdateConceptsResultBo() {
		return new UpdateConceptsResultBo(
				3, // cargados
				1, // erroneos
				Collections.emptyList(), // errors
				0
		);
	}
}