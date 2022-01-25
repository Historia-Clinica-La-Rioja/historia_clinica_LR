package net.pladema.snvs.domain.report;

import lombok.Getter;
import lombok.Setter;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.domain.report.exceptions.ReportCommandBoEnumException;
import net.pladema.snvs.domain.report.exceptions.ReportCommandBoException;

@Getter
@Setter
public class ReportCommandBo {

	private final Integer patientId;

	private final Integer institutionId;

	private final Integer groupEventId;

	private final Integer eventId;

	private final Integer manualClassificationId;

	private Integer professionalId;

	private final SnvsProblemBo problemBo;

	public ReportCommandBo(Integer patientId, Integer institutionId, Integer manualClassificationId, Integer groupEventId, Integer eventId, SnvsProblemBo problemBo) throws ReportCommandBoException {
		if (patientId == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_PATIENT_ID,"El id del paciente es obligatorio");
		this.patientId = patientId;
		if (institutionId == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_INSTITUTION_ID,"El id de la institución es obligatorio");
		this.institutionId = institutionId;
		if (manualClassificationId == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_MANUAL_CLASSIFICATION_ID,"El id de la clasificación manual es obligatorio");
		this.manualClassificationId = manualClassificationId;
		if (problemBo == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_PROBLEM,"La información del problema es obligatoria");
		this.problemBo = problemBo;
		if (groupEventId == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_GROUP_EVENT_ID,"El id de grupo evento es obligatorio");
		this.groupEventId = groupEventId;
		if (eventId == null)
			throw new ReportCommandBoException(ReportCommandBoEnumException.NULL_EVENT_ID,"El id de evento es obligatorio");
		this.eventId = eventId;
	}

	public ReportCommandBo(SnvsReportBo snvsReportBo){
		this.patientId = snvsReportBo.getPatientId();
		this.institutionId = snvsReportBo.getInstitutionId();
		this.groupEventId = snvsReportBo.getGroupEventId();
		this.eventId = snvsReportBo.getEventId();
		this.professionalId = snvsReportBo.getProfessionalId();
		this.manualClassificationId = snvsReportBo.getManualClassificationId();
		this.problemBo = snvsReportBo.getProblemBo();
	}
}
