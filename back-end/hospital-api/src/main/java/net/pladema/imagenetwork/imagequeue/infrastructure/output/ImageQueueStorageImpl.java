package net.pladema.imagenetwork.imagequeue.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.imagequeue.application.port.ImageQueueStorage;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageQueueStorageImpl implements ImageQueueStorage {

    private static final String RESULT_OK = "200";

    private final MoveStudiesRepository moveStudiesRepository;

    @Override
    public List<ImageQueueBo> getStudiesInQueue(Integer institutionId) {
        return moveStudiesRepository.findImagesNotMovedByInstitutionIdAndResultNot(institutionId, RESULT_OK);
    }

}
