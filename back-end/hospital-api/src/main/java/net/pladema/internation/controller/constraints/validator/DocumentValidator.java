package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.repository.documents.DocumentRepository;
import net.pladema.internation.repository.documents.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DocumentValidator implements ConstraintValidator<DocumentValid, Object[]> {

    private boolean confirmed;
    private short documentType;

    private static final Map<Short, String> properties = new HashMap<>();

    private final DocumentRepository documentRepository;

    public DocumentValidator(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
        properties.put(DocumentType.ANAMNESIS, "anamnesisId");
        properties.put(DocumentType.EVALUATION_NOTE, "evolutionNoteId");
        properties.put(DocumentType.EPICRISIS, "epicrisisId");
    }

    @Override
    public void initialize(DocumentValid constraintAnnotation) {
        this.confirmed = constraintAnnotation.isConfirmed();
        this.documentType = constraintAnnotation.documentType();
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Long documentId = (Long)parameters[2];
        Optional<Document> document = documentRepository.findById(documentId);

        String statusId = confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{document.invalid}")
                .addPropertyNode(properties.get(documentType))
                .addConstraintViolation();

        return document.isPresent() //existencia
                && document.get().isType(documentType)
                && document.get().hasStatus(statusId);
    }
}
