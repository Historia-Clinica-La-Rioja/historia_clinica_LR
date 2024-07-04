package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListStudyOrderReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyOrderReportInfoService;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListStudyOrderReportInfoServiceImpl implements ListStudyOrderReportInfoService {

    private final ListStudyOrderReportRepository listStudyOrderReportRepository;
    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

    @Override
    public List<StudyOrderReportInfoBo> getListStudyOrder(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyOrderReportInfoBo> result = listStudyOrderReportRepository.execute(patientId).stream()
                .map(this::createStudyOrderReportInfoBo)
                .peek(studyOrderReportInfoBo -> studyOrderReportInfoBo.setIsAvailableInPACS(
                        getPacWhereStudyIsHosted.run(studyOrderReportInfoBo.getImageId(), false)
                                .isAvailableInPACS()))
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private StudyOrderReportInfoBo createStudyOrderReportInfoBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        StudyOrderReportInfoBo result = new StudyOrderReportInfoBo();
        result.setStatus((Boolean) row[0]);
        result.setDoctorUserId((Integer) row[1]);
        result.setCreationDate(((Timestamp) row[2]).toLocalDateTime());
        result.setImageId((String) row[3]);
        result.setDocumentId((BigInteger) row[4]);
        result.setSnomed((String) row[5]);
        result.setHealthCondition((String) row[6]);
        result.setFileName((String) row[7]);
        result.setSource((String) row[8]);
        result.setServiceRequestId((Integer) row[9]);
        result.setDiagnosticReportId((Integer) row[10]);
        result.setHasActiveAppointment((Boolean) row[11]);
        result.setObservationsFromServiceRequest((String) row[12]);
        log.trace("Output -> {}", result);
        return result;
    }
}
