package net.pladema.imagenetwork.imagequeue.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class MoveImageBo {

    private final Integer id;
    private final Integer instututionId;
    private final EImageMoveStatus imageMoveStatus;

}
