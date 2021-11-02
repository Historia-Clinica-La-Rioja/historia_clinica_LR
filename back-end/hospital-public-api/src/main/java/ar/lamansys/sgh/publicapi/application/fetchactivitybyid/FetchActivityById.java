package ar.lamansys.sgh.publicapi.application.fetchactivitybyid;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FetchActivityById {

    private final Logger logger;
    private final ActivityStorage activityStorage;

    public FetchActivityById(ActivityStorage activityStorage) {
        this.logger = LoggerFactory.getLogger(FetchActivityById.class);
        this.activityStorage = activityStorage;
    }

    public AttentionInfoBo run(String refsetCode, String provinceCode, Long activityId) {
        logger.debug("Input parameters -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);
        AttentionInfoBo result = activityStorage.getActivityById(refsetCode, provinceCode, activityId).orElse(null);
        logger.debug("Output -> {}", result);
        return result;
    }
}
