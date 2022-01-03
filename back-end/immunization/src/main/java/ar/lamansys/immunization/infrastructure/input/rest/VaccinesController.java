package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.fetchVaccines.FetchVaccineById;
import ar.lamansys.immunization.infrastructure.input.rest.dto.VaccineInformationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vaccines")
@Tag(name = "Vaccines", description = "Vaccines")
@Validated
public class VaccinesController {

    private final Logger logger;

    private final FetchVaccineById fetchVaccineById;

    public VaccinesController(FetchVaccineById fetchVaccineById) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.fetchVaccineById = fetchVaccineById;
    }

    @GetMapping("/{vaccineSctid}")
    @ResponseBody
    public VaccineInformationDto vaccineInformation(@PathVariable("vaccineSctid") String vaccineSctid) {
        var vaccine = fetchVaccineById.run(vaccineSctid);
        var result = vaccine != null ? new VaccineInformationDto(vaccine) : null;
        logger.debug("Output -> {}", result);
        return result;
    }

}