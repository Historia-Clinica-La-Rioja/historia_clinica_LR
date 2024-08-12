package net.pladema.imagenetwork.imagequeue.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.imagenetwork.imagequeue.application.port.ImageQueueStorage;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.MoveImageBo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageQueueStorageImpl implements ImageQueueStorage {

    private static final String RESULT_OK = "200";
    private static final String RESULT_EMPTY = "";
    private static final Integer ZERO_ATTEMPTS = 0;

    private final MoveStudiesRepository moveStudiesRepository;

    @Override
    public List<ImageQueueBo> getStudiesInQueue(Integer institutionId, LocalDate from, LocalDate to) {
        return moveStudiesRepository.findImagesNotMovedByInstitutionId(
                institutionId,
                from,
                to,
                RESULT_OK);
    }

    @Override
    public Optional<MoveImageBo> getImageInQueue(Integer moveImageId) {
        return moveStudiesRepository.findById(moveImageId).map(this::mapToBo);
    }

    @Override
    public Boolean updateStatusToPending(Integer imageId) {
        moveStudiesRepository.updateStatusAndResultAndAttemptsNumbre(
                imageId,
                EImageMoveStatus.PENDING.toString(),
                RESULT_EMPTY,
                ZERO_ATTEMPTS
        );
        return true;
    }

    @Override
    public Boolean updateImageUIDAndStatusToPending(Integer imageId, String newUID) {
        moveStudiesRepository.updateUIDAndStatusAndResultAndAttemptsNumber(
                imageId,
                newUID,
                EImageMoveStatus.PENDING.toString(),
                RESULT_EMPTY,
                ZERO_ATTEMPTS
        );
        return true;
    }

    private MoveImageBo mapToBo(MoveStudies moveStudies) {
        return MoveImageBo.builder()
                .id(moveStudies.getId())
                .instututionId(moveStudies.getInstitutionId())
                .imageMoveStatus(EImageMoveStatus.map(moveStudies.getStatus()))
                .build();
    }

}
