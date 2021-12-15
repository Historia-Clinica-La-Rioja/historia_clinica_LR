package ar.lamansys.sgx.shared.restclient.infrastructure.input.scheduling;

import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CleanRestClientMeasureScheduler {

	private final RestClientMeasureRepository restClientMeasureRepository;

	public CleanRestClientMeasureScheduler(RestClientMeasureRepository restClientMeasureRepository) {
		this.restClientMeasureRepository = restClientMeasureRepository;
	}

	@Scheduled(cron = "0 0 0 * * *")
	@SchedulerLock(name = "CleanRestClientMeasureScheduler")
	public void run() {
		log.info("Clean rest client measure");
		restClientMeasureRepository.deleteAll();
	}
}