package net.pladema.template.infrastructure.input.rest.constraints.validator;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.enums.EDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.constraints.ValidTypeDocumentTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
@Slf4j
public class TypeDocumentTemplateValidator implements ConstraintValidator<ValidTypeDocumentTemplate, Object[]>  {

    @Override
    public void initialize(ValidTypeDocumentTemplate constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Short typeId = (Short) parameters[1];
        log.debug("Input parameters -> typeId {}", typeId);

        boolean result = true;

        try {
            EDocumentTemplate.map(typeId);
        } catch(NotFoundException e) {
            log.debug(e.getMessage());
            buildResponse(context, "{document.template.error.type.not-found}");
            result = false;
        }

        return result;

    }

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
