package net.pladema.imagenetwork.application.getviewerurl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;
import net.pladema.imagenetwork.domain.ViewerUrlBo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetViewerUrl {

    @Value("${app.imagenetwork.viewer.web.url}")
    private String viewerUrl;

    public ViewerUrlBo run() {
        log.debug("Get url image network viewer web");
        return new ViewerUrlBo(this.getUrl());
    }

    private String getUrl() {
        if (viewerUrl == null || viewerUrl.isBlank())
            throw new StudyException(StudyExceptionEnum.VIEWER_URL_NOT_DEFINED, StudyExceptionEnum.VIEWER_URL_NOT_DEFINED.getMessage());
        return viewerUrl;
    }
}
