package net.pladema.imagenetwork.application.getviewerurl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.domain.exception.EStudyException;
import net.pladema.imagenetwork.domain.ViewerUrlBo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetViewerUrl {

    private static final String VIEWER_CONTEXT = "viewer";

    @Value("${app.imagenetwork.viewer.web.url}")
    private String viewerUrl;

    @PostConstruct
    void started() {
        if (viewerUrl == null || viewerUrl.isBlank())
            return;
        viewerUrl = UriComponentsBuilder.fromUriString(viewerUrl)
                .replacePath(VIEWER_CONTEXT)
                .build()
                .toUri()
                .toString();
    }

    public ViewerUrlBo run() {
        return new ViewerUrlBo(this.getUrl());
    }

    private String getUrl() {
        if (viewerUrl == null || viewerUrl.isBlank())
            throw new StudyException(EStudyException.VIEWER_URL_NOT_DEFINED, "app.imagenetwork.error.viewer-not-defined");
        return viewerUrl;
    }
}
