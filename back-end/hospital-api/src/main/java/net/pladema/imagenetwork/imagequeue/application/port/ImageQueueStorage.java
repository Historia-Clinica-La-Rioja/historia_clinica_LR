package net.pladema.imagenetwork.imagequeue.application.port;

import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;

import java.util.List;

public interface ImageQueueStorage {
    List<ImageQueueBo> getStudiesInQueue(Integer institutionId);
}
