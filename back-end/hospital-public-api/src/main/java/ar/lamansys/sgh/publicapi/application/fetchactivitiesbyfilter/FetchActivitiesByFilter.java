package ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchActivitiesByFilter {

    private final Logger logger;
    private final ActivityStorage activityStorage;

    public FetchActivitiesByFilter(ActivityStorage activityStorage) {
        this.logger = LoggerFactory.getLogger(FetchActivitiesByFilter.class);
        this.activityStorage = activityStorage;
    }

    public List<AttentionInfoBo> run(ActivitySearchFilter filter) {
        logger.debug("Input parameters -> filter {}", filter);
        List<AttentionInfoBo> result = getFromStorage(filter);
        logger.debug("Output -> {}", result);
        return result;
    }

    private List<AttentionInfoBo> getFromStorage(ActivitySearchFilter filter) {
        if (filter.getIdentificationNumber() != null)
            return activityStorage.getActivitiesByInstitutionAndPatient(
                    filter.getRefsetCode(), filter.getProvinceCode(), filter.getIdentificationNumber(), filter.getFrom(), filter.getTo(), filter.getReprocessing());
        if (filter.getCoverageCuit() != null)
            return activityStorage.getActivitiesByInstitutionAndCoverage(
                    filter.getRefsetCode(), filter.getProvinceCode(), filter.getCoverageCuit(), filter.getFrom(), filter.getTo(), filter.getReprocessing());
        return activityStorage.getActivitiesByInstitution(
                    filter.getRefsetCode(), filter.getProvinceCode(), filter.getFrom(), filter.getTo(), filter.getReprocessing());
    }
}
