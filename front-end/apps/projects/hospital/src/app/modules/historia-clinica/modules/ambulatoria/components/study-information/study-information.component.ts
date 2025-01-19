import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { StudyInformation } from '../../modules/estudio/components/study/study.component';
import { StudyInfo } from '../../services/study-results.service';

@Component({
	selector: 'app-study-information',
	templateUrl: './study-information.component.html',
	styleUrls: ['./study-information.component.scss']
})

export class StudyInformationComponent {

	@Input() patientId: number;
	@Input() diagnosticReport: DiagnosticReportInfoDto[] | StudyInformation[];
	@Input() studies: StudyInfo[];
}
