package net.pladema.report.infrastructure.output;

import lombok.extern.slf4j.Slf4j;
import net.pladema.report.application.ReportWSStorage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@ConditionalOnProperty(
		value="app.usage-metrics.enabled",
		havingValue = "false",
		matchIfMissing = true
)
@Service
public class ReportWSStorageVoidImpl implements ReportWSStorage {

	@Override
	public void saveReport(List<Map<String, Object>> reportData, String reportName) {
		log.warn("Saving ReportWS [DISABLED] -> reportName {}", reportName);
	}

	@Override
	public String healthCheck() {
		return HEALTH_CHECK_DISABLED;
	}

}
