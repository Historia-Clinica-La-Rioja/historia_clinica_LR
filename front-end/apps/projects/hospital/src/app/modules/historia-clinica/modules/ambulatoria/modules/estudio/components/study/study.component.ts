import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { PrescripcionesService, PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { Content, Title } from '@presentation/components/indication/indication.component';

@Component({
	selector: 'app-study',
	templateUrl: './study.component.html',
	styleUrls: ['./study.component.scss']
})
export class StudyComponent {

	@Input() studies: DiagnosticReportInfoDto[];
	@Input() studyHeader: Title;
	@Input() patientId: number;

	constructor(
		private readonly prescripcionesService: PrescripcionesService,
		private readonly translateService: TranslateService
	) { }

	contentBuilder(diagnosticReport: DiagnosticReportInfoDto): Content {
		const prescriptionStatus = this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId);
		return {
			status: {
				description: prescriptionStatus,
				cssClass: prescriptionStatus === this.translateService.instant('ambulatoria.paciente.studies.study_state.PENDING') ? 'red' : 'blue'
			},
			description: diagnosticReport.snomed.pt,
			extra_info: [{
				title: 'Razón:',
				content: diagnosticReport.healthCondition.snomed.pt
			}],
			createdBy: diagnosticReport.doctor.firstName + " " + diagnosticReport.doctor.lastName,
			timeElapsed: '06/05/98 - 12:00 hs.'
		}
	}

}
