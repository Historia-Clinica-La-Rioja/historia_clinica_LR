package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.imagequeue.application.getimagequeue.GetImageQueue;
import net.pladema.imagenetwork.imagequeue.application.imagemoveretry.ImageMoveRetry;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto.ImageQueueFilteringCriteriaDto;
import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto.ImageQueueListDto;
import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.mapper.ImageQueueMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Image Queue", description = "Image Queue")
@RequestMapping("/institutions/{institutionId}/image-queue")
@RestController
public class ImageQueueController {

    public static final String OUTPUT = "Output -> {}";

    private final GetImageQueue getImageQueue;
    private final ImageMoveRetry imageMoveRetry;
    private final ImageQueueMapper imageQueueMapper;


    @PreAuthorize("hasPermission(#institutionId, 'INDEXADOR')")
    @PostMapping
    public ResponseEntity<PageDto<ImageQueueListDto>> getImageQueueList(
            @PathVariable("institutionId") Integer institutionId,
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @RequestParam(name = "pageSize") Integer pageSize,
            @RequestBody @Valid ImageQueueFilteringCriteriaDto filteringCriteriaDto
    ) {
        log.debug("Input parameters -> institutionId {}", institutionId);

        List<ImageQueueBo> resultList = getImageQueue.run(
                institutionId,
                imageQueueMapper.fromImageQueueFilteringCriteriaDto(filteringCriteriaDto)
        );

        List<ImageQueueBo> currentPageList  =
                resultList.stream()
                        .skip((long) pageNumber * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList());

        List<ImageQueueListDto> mappedResult = imageQueueMapper.toImageQueueListDto(currentPageList);

        log.trace(OUTPUT, mappedResult);
        return ResponseEntity.ok(new PageDto<>(mappedResult,(long) resultList.size()));
    }

    @PreAuthorize("hasPermission(#institutionId, 'INDEXADOR')")
    @PutMapping("move-image/{moveImageId}/retry")
    public ResponseEntity<Boolean> retryMove(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("moveImageId") Integer moveImageId
    ) {
        log.debug("Input parameters -> institutionId {}, moveImageId {}", institutionId, moveImageId);
        Boolean result = imageMoveRetry.run(institutionId,moveImageId);
        log.trace(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

}


