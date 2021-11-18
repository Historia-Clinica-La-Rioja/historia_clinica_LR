package ar.lamansys.sgh.clinichistory.application.calculatecie10;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions.HCICIE10Exception;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions.HCICIE10ExceptionEnum;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import net.pladema.snowstorm.services.domain.Cie10RuleFeature;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CalculateCie10Facade {

    private final Logger logger;

    private final CalculateCie10CodesService calculateCie10CodesService;

    public CalculateCie10Facade(CalculateCie10CodesService calculateCie10CodesService) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.calculateCie10CodesService = calculateCie10CodesService;
    }

    public String execute(String sctid, Cie10FacadeRuleFeature features){
        logger.debug("Calculate cie10code to sctid -> {}, with this features -> {}", sctid, features);
        String result;
        try {
            result = calculateCie10CodesService.execute(sctid, map(features));
        } catch (SnowstormApiException e) {
            throw new HCICIE10Exception(HCICIE10ExceptionEnum.SNOWSTORM_TIMEOUT_SERVICE, e.getMessage());
        }
        return result;
    }

    private Cie10RuleFeature map(Cie10FacadeRuleFeature features) {
        return new Cie10RuleFeature(features.getGender(), features.getAge());
    }
}
