import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto } from '@api-rest/api-model';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-surgical-report-diagnosis',
	templateUrl: './surgical-report-diagnosis.component.html',
	styleUrls: ['./surgical-report-diagnosis.component.scss']
})
export class SurgicalReportDiagnosisComponent implements OnInit {

	@Input() diagnosis$: Observable<DiagnosisDto[]>;
	@Input() diagnosis: DiagnosisDto[];

	selectedDiagnosis: DiagnosisDto[] = [];
	form = this.formBuilder.group({
		diagnosis: new FormControl<DiagnosisDto[] | null>([]),
	});

	constructor(
		private formBuilder: FormBuilder,
		public dialog: MatDialog,

	) { }

	ngOnInit(): void { }

	setDiagnosis(diagnose: DiagnosisDto): void {
		if (diagnose)
			if (!this.selectedDiagnosis.includes(diagnose))
				this.selectedDiagnosis.push(diagnose);
	}

	deleteDiagnose(index: number): void {
		this.selectedDiagnosis.splice(index, 1);
	}

	addDiagnosis(): void {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: false
			}
		});

		dialogRef.afterClosed().subscribe(diagnose => {
			if (diagnose) {
				this.diagnosis.push(diagnose);
				this.selectedDiagnosis.push(diagnose);
			}
		});
	}
}
