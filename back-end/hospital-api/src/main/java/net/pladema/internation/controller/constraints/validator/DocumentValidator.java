package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.repository.core.DocumentRepository;
import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class DocumentValidator implements ConstraintValidator<DocumentValid, Object[]> {

    private boolean confirmed;
    private short documentType;

    private static final String ANAMNESIS_PROPERTY = "anamnesisId";

    private final DocumentRepository documentRepository;

    public DocumentValidator(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
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

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{document.update.invalid}")
                .addPropertyNode(ANAMNESIS_PROPERTY)
                .addConstraintViolation();

        String statusId = confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;

        return document.isPresent() //existencia
                && document.get().isType(documentType)
                && document.get().hasStatus(statusId);

    }
}
