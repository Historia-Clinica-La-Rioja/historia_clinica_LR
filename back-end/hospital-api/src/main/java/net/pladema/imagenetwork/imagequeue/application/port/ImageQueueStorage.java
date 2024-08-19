package net.pladema.imagenetwork.imagequeue.application.port;

import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.MoveImageBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ImageQueueStorage {

    List<ImageQueueBo> getStudiesInQueue(Integer institutionId, LocalDate from, LocalDate to);

    Optional<MoveImageBo> getImageInQueue(Integer moveImageId);

    Boolean updateStatusToPending(Integer id);
}
