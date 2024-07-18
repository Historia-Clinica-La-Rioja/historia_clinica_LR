import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto, ProblemTypeEnum, SurgicalReportDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-diagnosis',
	templateUrl: './surgical-report-diagnosis.component.html',
	styleUrls: ['./surgical-report-diagnosis.component.scss'],
})
export class SurgicalReportDiagnosisComponent implements OnInit{

	@Input() diagnosis: DiagnosisDto[];
	@Input() surgicalReport: SurgicalReportDto;
	@Input() mainDiagnosis: HealthConditionDto;

	constructor(
		public dialog: MatDialog
	) {}

	ngOnInit(){
		this.surgicalReport.mainDiagnosis = this.mainDiagnosis;
		this.diagnosis.forEach(diagnosis => diagnosis.type = ProblemTypeEnum.PREOPERATIVE_DIAGNOSIS);
		this.surgicalReport.preoperativeDiagnosis = this.diagnosis;
	}

	isEmpty(): boolean {
		return !this.surgicalReport.preoperativeDiagnosis?.length;
    }

	changeDiagnosis(): void {
		this.surgicalReport.mainDiagnosis = this.mainDiagnosis;
		this.diagnosis.forEach(diagnosis => diagnosis.type = ProblemTypeEnum.PREOPERATIVE_DIAGNOSIS);
		this.surgicalReport.preoperativeDiagnosis = this.diagnosis;
	}
}
