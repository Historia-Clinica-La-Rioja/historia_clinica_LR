package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.ProceduresValid;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HospitalizationProcedureDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProceduresValidator implements ConstraintValidator<ProceduresValid, List<HospitalizationProcedureDto>> {

    private static final Logger LOG = LoggerFactory.getLogger(ProceduresValidator.class);

    @Override
    public void initialize(ProceduresValid constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(List<HospitalizationProcedureDto> procedures, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> procedures {}", procedures);
        if (procedures == null || procedures.isEmpty())
            return true;

        final Set<HospitalizationProcedureDto> set = new HashSet<>();

        for (HospitalizationProcedureDto p : procedures)
            if (!set.add(p))
                return false;
        return true;
    }
}
