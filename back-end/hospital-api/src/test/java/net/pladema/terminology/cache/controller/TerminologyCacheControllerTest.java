package net.pladema.terminology.cache.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;

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
import net.pladema.terminology.cache.controller.dto.TerminologyCSVDto;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileIngestor;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileStorage;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileDownloadService;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileIngestService;
import net.pladema.terminology.cache.infrastructure.output.repository.SnomedCacheFileRepository;
import net.pladema.terminology.cache.jobs.DownloadCacheCSVJob;
import net.pladema.terminology.cache.jobs.IngestCacheCSVJob;


@ExtendWith(MockitoExtension.class)
class TerminologyCacheControllerTest extends UnitRepository {

	@Autowired
	private SnomedCacheFileRepository snomedCacheFileRepository;

	@Mock
	private SnomedCacheFileStorage snomedCacheFileStorage;

	@Mock
	private SnomedCacheFileIngestor snomedCacheFileIngestor;

	private DownloadCacheCSVJob downloadJob;
	private IngestCacheCSVJob ingestJob;

	private TerminologyCacheController controller;

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

		controller.addCSV(
				new TerminologyCSVDto(SnomedECL.DIAGNOSIS, "http://lamansys.ar/ecl/ECL.csv")
		);

		var queue = controller.getQueue();

		assertThat(queue).hasSize(1);

		var queueItem = queue.get(0);

		assertThat(queueItem)
				.hasFieldOrPropertyWithValue("url", "http://lamansys.ar/ecl/ECL.csv")
				.hasFieldOrPropertyWithValue("ecl", SnomedECL.DIAGNOSIS)
				.hasFieldOrPropertyWithValue("downloadedError", null)
		;

	}

	@Test
	void addCSV_Download() {

		controller.addCSV(
				new TerminologyCSVDto(SnomedECL.ALLERGY, "http://lamansys.ar/ecl/ECL.csv")
		);

		when(snomedCacheFileStorage.save(anyString()))
				.thenThrow(new RuntimeException("Imposible leer"));
		downloadJob.execute();

		var queue = controller.getQueue();

		assertThat(queue).hasSize(1);

		var queueItem = queue.get(0);

		assertThat(queueItem)
				.hasFieldOrPropertyWithValue("url", "http://lamansys.ar/ecl/ECL.csv")
				.hasFieldOrPropertyWithValue("ecl", SnomedECL.ALLERGY)
				.hasFieldOrPropertyWithValue("downloadedError", "Imposible leer")
		;
	}

	@Test
	void addCSV_Download_Ingest() {

		controller.addCSV(
				new TerminologyCSVDto(SnomedECL.ALLERGY, "http://lamansys.ar/ecl/ECL.csv")
		);

		when(snomedCacheFileStorage.save(anyString()))
				.thenReturn(newFileInfo(18L));

		assertThat(controller.getQueue())
				.hasSize(1);

		downloadJob.execute();

		assertThat(controller.getQueue())
				.hasSize(1);

		when(snomedCacheFileIngestor.run(anyLong(), anyString()))
				.thenReturn(newUpdateConceptsResultBo("ECL"));

		ingestJob.execute();

		assertThat(controller.getQueue())
				.isEmpty();

	}

	@Test
	void addCSV_Download_Injest_Multiple() {

		controller.addCSV(
				new TerminologyCSVDto(SnomedECL.ALLERGY, "http://lamansys.ar/ecl/ECL.csv")
		);
		controller.addCSV(
				new TerminologyCSVDto(SnomedECL.DIAGNOSIS, "http://lamansys.ar/ecl/ECL2.csv")
		);

		assertThat(controller.getQueue())
				.hasSize(2);


		when(snomedCacheFileStorage.save(anyString()))
				.thenReturn(newFileInfo(18L));
		downloadJob.execute();

		when(snomedCacheFileIngestor.run(anyLong(), anyString()))
				.thenReturn(newUpdateConceptsResultBo("ECL"));

		ingestJob.execute();

		assertThat(controller.getQueue())
				.hasSize(1);

		when(snomedCacheFileStorage.save(anyString()))
				.thenReturn(newFileInfo(19L));
		downloadJob.execute();

		when(snomedCacheFileIngestor.run(anyLong(), anyString()))
				.thenThrow(new RuntimeException("Failed to parse CSV file: (line 28) invalid char between encapsulated token and delimiter"));

		ingestJob.execute();

		assertThat(controller.getQueue())
				.hasSize(1);

	}

	private static FileInfo newFileInfo(Long id) {
		var fileInfo = new FileInfo();
		fileInfo.setId(id);
		return fileInfo;
	}

	private static UpdateConceptsResultBo newUpdateConceptsResultBo(String ecl) {
		return new UpdateConceptsResultBo(
				ecl,
				3, // cargados
				1, // erroneos
				Collections.emptyList()
		);
	}
}