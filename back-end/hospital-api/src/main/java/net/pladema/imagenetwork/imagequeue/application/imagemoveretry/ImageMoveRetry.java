package net.pladema.imagenetwork.imagequeue.application.imagemoveretry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.imagequeue.application.imagemoveretry.exceptions.ImageQueueException;
import net.pladema.imagenetwork.imagequeue.application.port.ImageQueueStorage;
import net.pladema.imagenetwork.imagequeue.application.validator.ImageQueueValidator;
import net.pladema.imagenetwork.imagequeue.domain.MoveImageBo;
import net.pladema.imagenetwork.imagequeue.domain.exceptions.ImageQueueEnumException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageMoveRetry {

    private final ImageQueueStorage imageQueueStorage;
    private final ImageQueueValidator imageQueueValidator;

    @Transactional
    public Boolean run(Integer institutionId, Integer moveImageId) {
        log.debug("Input parameters -> institutionId {}, moveImageId {}", institutionId, moveImageId);

        MoveImageBo imageToUpdate = imageQueueStorage.getImageInQueue(moveImageId).orElseThrow(
                () -> new ImageQueueException(
                        ImageQueueEnumException.IMAGE_IN_QUEUE_NOT_FOUND,
                        "La imagen seleccionada no se encuentra en la cola"
                )
        );
        imageQueueValidator.assertSameInstitution(imageToUpdate.getInstututionId(),institutionId);
        imageQueueValidator.assertStatusError(imageToUpdate.getImageMoveStatus());

        Boolean retryResult = imageQueueStorage.updateStatusToPending(imageToUpdate.getId());

        log.debug("Output -> retryResult {}", retryResult);
        return retryResult;

    }
}
