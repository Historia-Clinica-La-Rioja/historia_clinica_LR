package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.ProbableDischargeDateValid;
import net.pladema.clinichistory.hospitalization.controller.dto.ProbableDischargeDateDto;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ProbableDischargeDateValidator implements ConstraintValidator<ProbableDischargeDateValid, Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(ProbableDischargeDateValidator.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final LocalDateMapper localDateMapper;

    public ProbableDischargeDateValidator(InternmentEpisodeService internmentEpisodeService,
                                          LocalDateMapper localDateMapper){
        this.internmentEpisodeService = internmentEpisodeService;
        this.localDateMapper = localDateMapper;
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext constraintValidatorContext) {
        Integer internmentEpisodeId = (Integer) parameters[1];
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        LocalDate entryDate = internmentEpisodeService.getEntryDate(internmentEpisodeId);

        ProbableDischargeDateDto probableDischargeDateDto = (ProbableDischargeDateDto) parameters[2];
        LocalDateTime probableDischargeDate = localDateMapper.fromStringToLocalDateTime(probableDischargeDateDto.getProbableDischargeDate());
        boolean result = probableDischargeDate.isAfter(entryDate.atStartOfDay());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
