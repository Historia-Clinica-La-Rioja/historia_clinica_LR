package net.pladema.snvs.application.reportproblems;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.snvs.application.ports.event.SnvsStorage;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.institution.InstitutionStorage;
import net.pladema.snvs.application.ports.patient.PatientStorage;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemEnumException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.report.ReportCommandBo;
import net.pladema.snvs.domain.report.SnvsToReportBo;
import org.springframework.stereotype.Service;

@Service
public class ReportBuilder {

	private final PatientStorage patientStorage;

	private final InstitutionStorage institutionStorage;

	private final SnvsStorage snvsStorage;

	private final DateTimeProvider dateTimeProvider;

	public ReportBuilder(PatientStorage patientStorage, InstitutionStorage institutionStorage, SnvsStorage snvsStorage, DateTimeProvider dateTimeProvider) {
		this.patientStorage = patientStorage;
		this.institutionStorage = institutionStorage;
		this.snvsStorage = snvsStorage;
		this.dateTimeProvider = dateTimeProvider;
	}

	public SnvsToReportBo buildReport(ReportCommandBo reportCommandBo) throws ReportProblemException, SnvsEventInfoBoException, SnvsStorageException {
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
		return new SnvsToReportBo(snvsEventInfo, dateTimeProvider.nowDate(), patient, institution, reportCommandBo.getProfessionalId());
	}
}
