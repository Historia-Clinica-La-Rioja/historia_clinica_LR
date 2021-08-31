package ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo;

import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.exceptions.FetchVaccineConditionApplicationException;
import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.exceptions.FetchVaccineConditionApplicationExceptionEnum;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FetchVaccineConditionApplicationInfo {

    private final Logger logger;
    private final VaccineConditionApplicationStorage vaccineConditionApplicationStorage;

    public FetchVaccineConditionApplicationInfo(VaccineConditionApplicationStorage vaccineConditionApplicationStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.vaccineConditionApplicationStorage = vaccineConditionApplicationStorage;
    }

    public VaccineConditionApplicationBo run(Short id){
        logger.debug("Fetch vaccine condition application from id {}", id);
        return vaccineConditionApplicationStorage.fetchConditionApplicationById(id)
                .orElseThrow(() -> new FetchVaccineConditionApplicationException(
                        FetchVaccineConditionApplicationExceptionEnum.UNKNOWN_CONDITION_APPLICATION,
                        String.format("El id %s es desconocido", id)));
    }
}
