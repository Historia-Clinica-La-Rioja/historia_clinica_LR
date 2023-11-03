package net.pladema.template.domain.exceptions;

import lombok.Getter;

@Getter
public class DocumentTemplateException extends RuntimeException {
    private final EDocumentTemplateException code;

    public DocumentTemplateException(EDocumentTemplateException code, String message) {
        super(message);
        this.code = code;
    }
}
