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


    public VaccineBo run(Short id) {
        logger.debug("FetchVaccineById {}", id);
        assertInfo(id);
        var result = vaccineStorage.findById(id)
                .orElseThrow(() -> new FetchVaccineByIdException(FetchVaccineByIdExceptionEnum.INVALID_VACCINE_ID,
                        String.format("La vacuna con id %s no existe", id)));
        logger.trace("Vaccines result -> {}", result);
        return result;
    }

    private void assertInfo(Short vaccineId) {
        Objects.requireNonNull(vaccineId, () -> {
            throw new FetchVaccineByIdException(FetchVaccineByIdExceptionEnum.NULL_VACCINE_ID, "El identificador de la vacuna es obligatorio");
        });
    }
}
