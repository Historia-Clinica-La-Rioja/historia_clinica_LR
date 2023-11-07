import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto } from '@api-rest/api-model';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { Procedimiento, ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-diagnosis',
	templateUrl: './surgical-report-diagnosis.component.html',
	styleUrls: ['./surgical-report-diagnosis.component.scss']
})
export class SurgicalReportDiagnosisComponent implements OnInit {

	@Input() diagnosis: DiagnosisDto[];

	selectedDiagnosis: DiagnosisDto[] = [];
	form = this.formBuilder.group({
		diagnosis: new FormControl<DiagnosisDto[] | null>([]),
		surgery: new FormControl<Procedimiento[] | null>([]),
	});

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService);

	constructor(
		private formBuilder: FormBuilder,
		public dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
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
