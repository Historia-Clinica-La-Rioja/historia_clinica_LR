package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.validator;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.ProceduresValid;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientProcedureDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProceduresValidator implements ConstraintValidator<ProceduresValid, List<OutpatientProcedureDto>> {

    private static final Logger LOG = LoggerFactory.getLogger(ProceduresValidator.class);

    @Override
    public void initialize(ProceduresValid constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(List<OutpatientProcedureDto> procedures, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> procedures {}", procedures);
        if (procedures == null || procedures.isEmpty())
            return true;

        final Set<OutpatientProcedureDto> set = new HashSet<>();

        for (OutpatientProcedureDto p : procedures)
            if (!set.add(p))
                return false;
        return true;
    }
}
