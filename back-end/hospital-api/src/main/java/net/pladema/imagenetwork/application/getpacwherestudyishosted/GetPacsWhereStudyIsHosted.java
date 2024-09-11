package net.pladema.imagenetwork.application.getpacwherestudyishosted;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.PacsCommunicationPort;
import net.pladema.imagenetwork.application.port.StudyPacAssociationStorage;
import net.pladema.imagenetwork.domain.PacsListBo;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPacsWhereStudyIsHosted {

    private final StudyPacAssociationStorage studyStorage;
    private final PacsCommunicationPort pacsCommunicationPort;

    public PacsListBo run(String studyInstanceUID, boolean doHealthcheck) {
        log.debug("Get PAC URL where the study '{}' is hosted", studyInstanceUID);
        var pacListBo = studyStorage.getPacServersBy(studyInstanceUID);
        var result = doHealthcheck
                ? this.filterUnrecheablePACS(pacListBo)
                : pacListBo;
        log.debug("Output -> {}", result);
        return result;
    }

    private PacsListBo filterUnrecheablePACS(PacsListBo pacs) {
        var result = pacs.getPacs()
                .stream()
                .map(pacsCommunicationPort::doHealthcheckProof)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        pacs.setPacs(result);
        return pacs;
    }
}
