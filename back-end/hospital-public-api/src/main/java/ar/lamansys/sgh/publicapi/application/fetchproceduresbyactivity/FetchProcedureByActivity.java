package ar.lamansys.sgh.publicapi.application.fetchproceduresbyactivity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;

@Service
public class FetchProcedureByActivity {

	private final Logger logger;
	private final ActivityInfoStorage activityInfoStorage;

	public FetchProcedureByActivity(ActivityInfoStorage activityInfoStorage) {
		this.logger = LoggerFactory.getLogger(FetchProcedureByActivity.class);
		this.activityInfoStorage = activityInfoStorage;
	}

	public List<ProcedureInformationBo> run(String refsetCode, Long activityId) {
		logger.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		var result = activityInfoStorage.getProceduresByActivity(refsetCode, activityId);
		logger.debug("Output -> {}", result);
		return result;
	}
}