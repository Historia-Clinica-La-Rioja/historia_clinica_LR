package net.pladema.template.infrastructure.input.rest.constraints.validator;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.existsname.ExistsNameDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.constraints.ValidDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.DocumentTemplateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
@Slf4j
public class DocumentTemplateValidator implements ConstraintValidator<ValidDocumentTemplate, Object[]> {

    private final ExistsNameDocumentTemplate existsNameDocumentTemplate;

    @Override
    public void initialize(ValidDocumentTemplate constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        DocumentTemplateDto templateDto = (DocumentTemplateDto) parameters[1];
        Integer userId = UserInfo.getCurrentAuditor();
        String name = templateDto.getName();
        String templateText = templateDto.getTemplateText();
        log.debug("Input parameters -> userId {}, name {}", userId, name);

        boolean result = true;

        if (existsNameDocumentTemplate.run(userId, name)) {
            log.debug("The user {} is trying to save a document template with name '{}'", userId, name);
            buildResponse(context, "{document.template.error.exist.name}");
            result = false;
        }

        if (templateText.isEmpty() || templateText.isBlank()) {
            log.debug("Trying to save an empty document template");
            buildResponse(context, "{document.template.error.empty.text}");
            result = false;
        }

        return result;
    }

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
