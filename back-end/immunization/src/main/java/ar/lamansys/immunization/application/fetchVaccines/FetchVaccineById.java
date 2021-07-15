package ar.lamansys.immunization.application.fetchVaccines;

import ar.lamansys.immunization.application.fetchVaccines.exceptions.FetchVaccineByIdException;
import ar.lamansys.immunization.application.fetchVaccines.exceptions.FetchVaccineByIdExceptionEnum;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FetchVaccineById {

    private final Logger logger;

    private final VaccineStorage vaccineStorage;

    public FetchVaccineById(VaccineStorage vaccineStorage) {
        this.vaccineStorage = vaccineStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    public VaccineBo run(String sctid) {
        logger.debug("FetchVaccineById {}", sctid);
        assertInfo(sctid);
        var result = vaccineStorage.findById(sctid).orElse(null);
        logger.trace("Vaccines result -> {}", result);
        return result;
    }

    private void assertInfo(String sctid) {
        Objects.requireNonNull(sctid, () -> {
            throw new FetchVaccineByIdException(FetchVaccineByIdExceptionEnum.NULL_VACCINE_ID, "El identificador de la vacuna es obligatorio");
        });
    }
}
