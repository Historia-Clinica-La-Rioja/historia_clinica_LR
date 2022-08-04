package ar.lamansys.sgh.publicapi.application.processactivity;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessActivity {

    private final Logger logger;
    private final ActivityInfoStorage activityInfoStorage;

    public ProcessActivity(ActivityInfoStorage activityInfoStorage) {
        this.logger = LoggerFactory.getLogger(ProcessActivity.class);
        this.activityInfoStorage = activityInfoStorage;
    }

    public void run(String refsetCode, String provinceCode, Long activityId) {
        logger.debug("Input parameters -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);
        activityInfoStorage.processActivity(refsetCode, provinceCode, activityId);
    }
}
