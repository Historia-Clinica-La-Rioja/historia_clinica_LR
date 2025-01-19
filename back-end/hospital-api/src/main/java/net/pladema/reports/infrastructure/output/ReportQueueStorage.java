package net.pladema.reports.infrastructure.output;

import java.util.Optional;

import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

public interface ReportQueueStorage {

	Optional<ReportQueue> findFirstToProcess();

	void generated(ReportQueue reportQueue, Long fileId);

	void generated(ReportQueue reportQueue, String message);

	void generating(ReportQueue reportQueue);
}
