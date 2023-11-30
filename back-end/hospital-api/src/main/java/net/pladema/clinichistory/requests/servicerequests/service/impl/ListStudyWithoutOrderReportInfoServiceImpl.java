package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListStudyWithoutOrderReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyWithoutOrderReportInfoService;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListStudyWithoutOrderReportInfoServiceImpl implements ListStudyWithoutOrderReportInfoService {

    private final ListStudyWithoutOrderReportRepository listStudyWithoutOrderReportRepository;
    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

    @Override
    public List<StudyWithoutOrderReportInfoBo> execute(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyWithoutOrderReportInfoBo> result = listStudyWithoutOrderReportRepository.execute(patientId).stream()
                .map(this::createDiagnosticReportBo)
                .peek(studyWithoutOrderReportInfoBo -> studyWithoutOrderReportInfoBo.setIsAvailableInPACS(
                        getPacWhereStudyIsHosted.run(studyWithoutOrderReportInfoBo.getImageId(), false)
                                .isAvailableInPACS()))
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private StudyWithoutOrderReportInfoBo createDiagnosticReportBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        StudyWithoutOrderReportInfoBo result = new StudyWithoutOrderReportInfoBo();
        result.setStatus((Boolean) row[0]);
        result.setImageId((String) row[1]);
        result.setDocumentId((BigInteger) row[2]);
        result.setFileName((String) row[3]);
        result.setDocumentStatus((String) row[4]);
        log.trace("Output -> {}", result);
        return result;
    }

}