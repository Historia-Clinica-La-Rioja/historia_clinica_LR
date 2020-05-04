package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.UpdateDocumentValid;
import net.pladema.internation.repository.core.DocumentRepository;
import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class UpdateDocumentValidator implements ConstraintValidator<UpdateDocumentValid, Object[]> {

    private static final String ANAMNESIS_PROPERTY = "anamnesisId";

    private final DocumentRepository documentRepository;

    public UpdateDocumentValidator(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    @Override
    public void initialize(UpdateDocumentValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Long documentId = (Long)parameters[2];
        Document document = documentRepository.getOne(documentId);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{}")
                .addPropertyNode(ANAMNESIS_PROPERTY)
                .addConstraintViolation();

        return document != null
                && document.getStatusId().equals(DocumentStatus.DRAFT)
                && document.getTypeId().equals(DocumentType.ANAMNESIS);
    }
}
