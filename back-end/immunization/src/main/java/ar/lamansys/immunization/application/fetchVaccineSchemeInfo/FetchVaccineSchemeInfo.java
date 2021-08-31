package ar.lamansys.immunization.application.fetchVaccineSchemeInfo;

import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.exceptions.FetchVaccineSchemeException;
import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.exceptions.FetchVaccineSchemeExceptionEnum;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FetchVaccineSchemeInfo {

    private final Logger logger;
    private final VaccineSchemeStorage vaccineSchemeStorage;

    public FetchVaccineSchemeInfo(VaccineSchemeStorage vaccineSchemeStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.vaccineSchemeStorage = vaccineSchemeStorage;
    }

    public VaccineSchemeBo run(Short id){
        logger.debug("Fetch scheme from id {}", id);
        return vaccineSchemeStorage.fetchSchemeById(id)
                .orElseThrow(() -> new FetchVaccineSchemeException(
                        FetchVaccineSchemeExceptionEnum.UNKNOWN_SCHEME,
                        String.format("El id %s es desconocido", id)));
    }
}
