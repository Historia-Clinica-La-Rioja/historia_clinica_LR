package ar.lamansys.sgh.publicapi.application.fetchproceduresbyactivity;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchProcedureByActivity {

    private final Logger logger;
    private final ActivityInfoStorage activityInfoStorage;

    public FetchProcedureByActivity(ActivityInfoStorage activityInfoStorage) {
        this.logger = LoggerFactory.getLogger(FetchProcedureByActivity.class);
        this.activityInfoStorage = activityInfoStorage;
    }

    public List<SnomedBo> run(String refsetCode, String provinceCode, Long activityId) {
        logger.debug("Input parameters -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);
        var result = activityInfoStorage.getProceduresByActivity(refsetCode, provinceCode, activityId);
        logger.debug("Output -> {}", result);
        return result;
    }
}
