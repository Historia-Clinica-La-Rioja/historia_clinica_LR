package net.pladema.reports.application.reportqueueprocess;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.reports.infrastructure.output.ReportQueueStorage;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@Slf4j
@AllArgsConstructor
@Service
public class ProcessReportQueueJob {
	private final GenerateReportFile generateReportFile;
	private final ReportQueueStorage queueStorage;

	public boolean isEmpty() {
		return queueStorage.findFirstToProcess().isEmpty();
	}
	@Scheduled(cron = "${scheduledjobs.process-report.cron}")
	@SchedulerLock(name = "ProcessReportQueueJob")
	public void run() {
		log.warn("Scheduled ProcessReportQueueJob starting at {}", new Date());

		var reportToProcessOpt = queueStorage.findFirstToProcess();
		if (reportToProcessOpt.isPresent()) {
			process(reportToProcessOpt.get());
		} else {
			log.debug("No hay IntitutionMonthlyReport para generar");
		}

		log.warn("Scheduled ProcessReportQueueJob done at {}", new Date());
	}

	private void process(ReportQueue reportQueue) {
		queueStorage.generating(reportQueue);
		try {
			var fileInfo = generateReportFile.run(reportQueue);
			queueStorage.generated(reportQueue, fileInfo.getId());
		} catch (Exception e) {
			queueStorage.generated(reportQueue, e.getMessage());
		}
	}


}
