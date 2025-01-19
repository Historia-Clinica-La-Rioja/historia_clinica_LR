package net.pladema.imagenetwork.imagequeue.application.imagemoveretry.exceptions;

import lombok.Getter;
import net.pladema.imagenetwork.imagequeue.domain.exceptions.ImageQueueEnumException;

@Getter
public class ImageQueueException extends RuntimeException {

    private final ImageQueueEnumException code;

    public ImageQueueException(ImageQueueEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
