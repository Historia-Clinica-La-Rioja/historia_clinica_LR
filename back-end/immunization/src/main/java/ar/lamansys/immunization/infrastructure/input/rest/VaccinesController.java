package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.fetchVaccines.FetchVaccineById;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.infrastructure.input.rest.dto.VaccineInformationDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vaccines")
@Api(value="Vaccines", tags= { "Vaccines" } )
@Validated
public class VaccinesController {

    private final Logger logger;

    private final FetchVaccineById fetchVaccineById;

    public VaccinesController(FetchVaccineById fetchVaccineById) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.fetchVaccineById = fetchVaccineById;
    }

    @GetMapping("/{vaccineId}")
    @ResponseBody
    public VaccineInformationDto get(@RequestParam("vaccineId") Short vaccineId) {
        var result = new VaccineInformationDto(fetchVaccineById.run(vaccineId));
        logger.debug("Output -> {}", result);
        return result;
    }

}