import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-diagnosis',
	templateUrl: './surgical-report-diagnosis.component.html',
	styleUrls: ['./surgical-report-diagnosis.component.scss']
})
export class SurgicalReportDiagnosisComponent implements OnInit {

	@Input() diagnosis: DiagnosisDto[];
	@Output() preoperativeDiagnosisChange = new EventEmitter();
	@Output() surgeryProceduresChange = new EventEmitter();

	selectedDiagnosis: DiagnosisDto[] = [];

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService);
	searchConceptsLocallyFF = false;

	constructor(
		private formBuilder: FormBuilder,
		public dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeSurgeryProcedure(procedures));
	}

	ngOnInit(): void { }

	setDiagnosis(diagnose: DiagnosisDto): void {
		if (diagnose)
			if (!this.selectedDiagnosis.includes(diagnose)) {
				this.selectedDiagnosis.push(diagnose);
				this.preoperativeDiagnosisChange.emit(this.selectedDiagnosis);
			}
	}

	deleteDiagnose(index: number): void {
		this.selectedDiagnosis.splice(index, 1);
		this.preoperativeDiagnosisChange.emit(this.selectedDiagnosis);
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
				this.preoperativeDiagnosisChange.emit(this.selectedDiagnosis);
			}
		});
	}

	addProcedure() {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.procedureService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFF,
				hideDate: true
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	private changeSurgeryProcedure(procedures): void {
		this.surgeryProceduresChange.emit(procedures);
	}
}
