import { Component, Input } from '@angular/core';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { Title } from '@presentation/components/indication/indication.component';

@Component({
	selector: 'app-study',
	templateUrl: './study.component.html',
	styleUrls: ['./study.component.scss']
})
export class StudyComponent {

	@Input() studies: DiagnosticReportInfoDto[];
	@Input() studyHeader: Title

	constructor() { }

}
