package ar.lamansys.sgh.publicapi.application.fetchsuppliesbyactivity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;

@Service
public class FetchSuppliesByActivity {

	private final Logger logger;
	private final ActivityInfoStorage activityInfoStorage;

	public FetchSuppliesByActivity(ActivityInfoStorage activityInfoStorage) {
		this.logger = LoggerFactory.getLogger(FetchSuppliesByActivity.class);
		this.activityInfoStorage = activityInfoStorage;
	}

	public List<SupplyInformationBo> run(String refsetCode, Long activityId) {
		logger.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		var result = activityInfoStorage.getSuppliesByActivity(refsetCode, activityId);
		logger.debug("Output -> {}", result);
		return result;
	}
}
