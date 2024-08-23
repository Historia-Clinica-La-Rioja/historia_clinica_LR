package net.pladema.imagenetwork.application.saveerrordownloadstudy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.StudyPacAssociationStorage;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveErrorDownloadStudy {

    private final StudyPacAssociationStorage studyPacAssociationStorage;

    @Transactional
    public Boolean run(ErrorDownloadStudyBo errorDownloadStudyBo) {
        log.debug("Input parameters -> errorDownloadStudyBo {}", errorDownloadStudyBo);

        errorDownloadStudyBo.setCreatedOn(LocalDateTime.now());
        this.assertContextValid(errorDownloadStudyBo);
        var result = studyPacAssociationStorage.saveErrorDownloadStudy(errorDownloadStudyBo).isPresent();

        log.debug("Output -> {}", result);
        return result;
    }

    private void assertContextValid(ErrorDownloadStudyBo errorDownloadStudyBo) {
        errorDownloadStudyBo.validateSelf();
    }
}
