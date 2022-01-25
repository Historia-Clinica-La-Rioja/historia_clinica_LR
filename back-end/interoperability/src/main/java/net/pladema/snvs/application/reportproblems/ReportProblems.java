package net.pladema.snvs.application.reportproblems;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.doctor.SnvsDoctorStorage;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.report.ReportPort;
import net.pladema.snvs.application.ports.report.ReportStorage;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemEnumException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.report.ReportCommandBo;
import net.pladema.snvs.domain.report.SnvsReportBo;
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

    private final SnvsDoctorStorage doctorStorage;

    private final ReportBuilder reportBuilder;

    public ReportProblems(ReportStorage reportStorage,
                          ReportPort reportPort,
                          SnvsDoctorStorage doctorStorage, ReportBuilder reportBuilder) {
        this.reportStorage = reportStorage;
        this.reportPort = reportPort;
        this.doctorStorage = doctorStorage;
        this.reportBuilder = reportBuilder;
    }

    @Transactional
    public List<SnvsReportBo> run(List<ReportCommandBo> toReportList) throws ReportPortException, SnvsEventInfoBoException,
            SnvsStorageException, ReportProblemException {
        List<SnvsReportBo> result = new ArrayList<>();
        for (ReportCommandBo reportCommandBo : toReportList) {
            validInput(reportCommandBo);
            var doctor = doctorStorage.getDoctorInfo()
                    .orElseThrow(() -> new ReportProblemException(ReportProblemEnumException.UNKNOWN_PROFESSIONAL, "El usuario no es médico"));
            SnvsReportBo response = reportPort.run(reportBuilder.buildReport(reportCommandBo));
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
}
