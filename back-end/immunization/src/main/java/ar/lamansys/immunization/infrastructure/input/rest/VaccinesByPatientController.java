package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.fetchVaccines.FetchVaccinesByPatient;
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
@RequestMapping("/immunization/vaccines-by-patient/{patientId}")
@Api(value="Vaccines", tags= { "Vaccines by patient" } )
@Validated
public class VaccinesByPatientController {

    private final Logger logger;

    private final FetchVaccinesByPatient fetchVaccinesByPatient;

    private final LocalDateMapper localDateMapper;

    public VaccinesByPatientController(FetchVaccinesByPatient fetchVaccinesByPatient,
                                       LocalDateMapper localDateMapper) {
        this.localDateMapper = localDateMapper;
        this.logger = LoggerFactory.getLogger(getClass());
        this.fetchVaccinesByPatient = fetchVaccinesByPatient;
    }

    @GetMapping
    @ResponseBody
    public List<VaccineInformationDto> get(@PathVariable("patientId") Integer patientId,
                                           @RequestParam("applicationDate")
                                           @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT) String applicationDate) {
        List<VaccineBo> result = fetchVaccinesByPatient.run(patientId, localDateMapper.fromStringToLocalDate(applicationDate));
        logger.debug("Output -> {}", result);
        return result.stream().map(VaccineInformationDto::new).collect(Collectors.toList());
    }

}