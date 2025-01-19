package net.pladema.imagenetwork.imagequeue.application.validator;


import net.pladema.imagenetwork.imagequeue.application.imagemoveretry.exceptions.ImageQueueException;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;
import net.pladema.imagenetwork.imagequeue.domain.exceptions.ImageQueueEnumException;
import org.springframework.stereotype.Component;

@Component
public class ImageQueueValidator {

    public void assertSameInstitution(Integer imageInstitutionId, Integer pathInstitutionId) {
        if (!pathInstitutionId.equals(imageInstitutionId)){
            throw new ImageQueueException(
                    ImageQueueEnumException.IMAGE_IN_QUEUE_NOT_FOUND_IN_INSTITUTION,
                    "La imagen no se encuentra en la institución seleccionada"
            );
        }
    }

    public void assertStatusError(EImageMoveStatus status) {
        if (EImageMoveStatus.MOVING.equals(status)) {
            throw new ImageQueueException(
                    ImageQueueEnumException.IMAGE_IN_QUEUE_ALREADY_MOVING,
                    "La imagen ya se esta moviendo"
            );
        }
        if (EImageMoveStatus.PENDING.equals(status)) {
            throw new ImageQueueException(
                    ImageQueueEnumException.IMAGE_IN_QUEUE_ALREADY_PENDING,
                    "La imagen ya se encuentra pendiente de moverse"
            );
        }
    }
}
