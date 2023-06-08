package net.pladema.imagenetwork.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.getviewerurl.GetViewerUrl;
import net.pladema.imagenetwork.domain.ViewerUrlBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.ViewerUrlDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/imagenetwork/viewer")
@Tag(name = "Image Network Viewer", description = "Image Network Viewer")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ViewerController {

    private final GetViewerUrl getViewerUrl;

    @GetMapping(value = "/url")
    public ResponseEntity<ViewerUrlDto> getUrl() {
        ViewerUrlDto viewerUrlDto = mapToDto(getViewerUrl.run());
        log.debug("Output -> {}", viewerUrlDto);
        return ResponseEntity.ok().body(viewerUrlDto);
    }

    private ViewerUrlDto mapToDto(ViewerUrlBo viewerUrlBo) {
        return ViewerUrlDto.builder()
                .url(viewerUrlBo.getUrl())
                .build();
    }
}
