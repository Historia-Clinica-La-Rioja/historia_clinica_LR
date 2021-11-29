package net.pladema.snvs.application.reportproblems;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.doctor.SnvsDoctorStorage;
import net.pladema.snvs.application.ports.event.SnvsStorage;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.institution.InstitutionStorage;
import net.pladema.snvs.application.ports.patient.PatientStorage;
import net.pladema.snvs.application.ports.report.ReportPort;
import net.pladema.snvs.application.ports.report.ReportStorage;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemEnumException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.report.ReportCommandBo;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.domain.report.SnvsToReportBo;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Conditional(SnvsCondition.class)
public class ReportProblems {

    private final ReportStorage reportStorage;

    private final ReportPort reportPort;

    private final PatientStorage patientStorage;

    private final InstitutionStorage institutionStorage;

    private final DateTimeProvider dateTimeProvider;

    private final SnvsStorage snvsStorage;

    private final SnvsDoctorStorage doctorStorage;

    public ReportProblems(ReportStorage reportStorage,
                          ReportPort reportPort,
                          PatientStorage patientStorage,
                          InstitutionStorage institutionStorage,
                          DateTimeProvider dateTimeProvider,
                          SnvsStorage snvsStorage, SnvsDoctorStorage doctorStorage) {
        this.reportStorage = reportStorage;
        this.reportPort = reportPort;
        this.patientStorage = patientStorage;
        this.institutionStorage = institutionStorage;
        this.dateTimeProvider = dateTimeProvider;
        this.snvsStorage = snvsStorage;
        this.doctorStorage = doctorStorage;
    }

    @Transactional
    public List<SnvsReportBo> run(List<ReportCommandBo> toReportList) throws ReportPortException, SnvsEventInfoBoException,
            SnvsStorageException, ReportProblemException {
        List<SnvsReportBo> result = new ArrayList<>();
        for (ReportCommandBo reportCommandBo : toReportList) {
            validInput(reportCommandBo);
            var doctor = doctorStorage.getDoctorInfo()
                    .orElseThrow(() -> new ReportProblemException(ReportProblemEnumException.UNKNOWN_PROFESSIONAL, "El usuario no es médico"));
            SnvsReportBo  response = reportPort.run(buildReport(reportCommandBo));
            response.setProfessionalId(doctor.getId());
            response.setProblemBo(reportCommandBo.getProblemBo());
            response = reportStorage.save(response);
            result.add(response);
        }
        return result;
    }

    private void validInput(ReportCommandBo reportCommandBo) throws ReportProblemException {
        if (reportCommandBo == null)
            throw new ReportProblemException(ReportProblemEnumException.NULL_REPORT,"No se pueden enviar reportes nulos");
    }

    private SnvsToReportBo buildReport(ReportCommandBo reportCommandBo) throws ReportProblemException, SnvsEventInfoBoException, SnvsStorageException {
        var patient = patientStorage.getPatientInfo(reportCommandBo.getPatientId())
                .orElseThrow(() -> new ReportProblemException(ReportProblemEnumException.UNKNOWN_PATIENT,
                        String.format("El paciente %s no existe", reportCommandBo.getPatientId())));
        var institution = institutionStorage.getInstitutionInfo(reportCommandBo.getInstitutionId())
                .orElseThrow(() -> new ReportProblemException(ReportProblemEnumException.UNKNOWN_INSTITUTION,
                        String.format("La institución %s no existe", reportCommandBo.getInstitutionId())));
        var snvsEventInfo = snvsStorage.fetchSnvsEventInfo(reportCommandBo.getProblemBo(),
                reportCommandBo.getManualClassificationId(), reportCommandBo.getGroupEventId(), reportCommandBo.getEventId())
                .orElseThrow(() -> new ReportProblemException(ReportProblemEnumException.UNKNOWN_EVENT,
                        String.format("No existe datos del evento=%s, grupo=%s para el problema=%s con la clasificación manual=%s",
                                reportCommandBo.getEventId(), reportCommandBo.getGroupEventId(), reportCommandBo.getProblemBo(), reportCommandBo.getManualClassificationId())));
        return new SnvsToReportBo(snvsEventInfo, dateTimeProvider.nowDate(), patient, institution);
    }
}
