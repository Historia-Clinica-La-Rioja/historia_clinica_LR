package ar.lamansys.sgx.shared.stats;

import java.time.Duration;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProfilingUtil {
	private final Instant start;
	private final String operation;
	private TimeProfilingUtil(String operation) {
		this.start = Instant.now();
		this.operation = operation;
	}

	public static TimeProfilingUtil start(String operation) {
		return new TimeProfilingUtil(operation);
	}

	public void done(String path) {
		Instant finish = Instant.now();
		long time = Duration.between(start, finish).toMillis();
		if (time > 3000) {
			log.warn("Long {} \"{}\": {} ms", operation, path, time);
		}
		log.debug("tracing {} \"{}\": {} ms", operation, path, time);
	}
}
