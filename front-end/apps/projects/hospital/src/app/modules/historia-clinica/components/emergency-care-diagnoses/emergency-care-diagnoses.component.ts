import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { getElementAtPosition } from '@core/utils/array.utils';
import { HEALTH_CLINICAL_STATUS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { DiagnosisCreationEditionComponent, DiagnosisMode } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/select-main-diagnosis/select-main-diagnosis.component';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-emergency-care-diagnoses',
	templateUrl: './emergency-care-diagnoses.component.html',
	styleUrls: ['./emergency-care-diagnoses.component.scss']
})
export class EmergencyCareDiagnosesComponent {
	allChecked = false;
	otherDiagnoses: EmergencyCareDiagnosis[];

	@Input() set listDiagnosis(diagnosticos: EmergencyCareDiagnosis[]) {
		this.otherDiagnoses = diagnosticos;
		this.allChecked = diagnosticos?.every(otherDiagnoses => otherDiagnoses.diagnosis.isAdded);
	};
	@Input() disableForm = false;
	@Input() mainDiagnosis: EmergencyCareMainDiagnosis;
	@Input() type: string;
	@Output() diagnosisChange = new EventEmitter();
	@Output() mainDiagnosisChange = new EventEmitter();

	constructor(
		public dialog: MatDialog,
		private snackBarService: SnackBarService,
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
	) { }

	private isMainDiagnosis(diagnosis: DiagnosisDto): boolean {
		return !this.otherDiagnoses.find(currentDiagnosis => currentDiagnosis.diagnosis.snomed.sctid === diagnosis.snomed.sctid) && diagnosis.snomed.sctid != this.mainDiagnosis?.main?.snomed.sctid
	}

	openCreationDialog(isMainDiagnosis: boolean) {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				diagnosisMode: DiagnosisMode.CREATION,
				isMainDiagnosis,
				hasPresumtiveMainDiagnosis: true
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				if (this.isMainDiagnosis(diagnosis)) {

					if (isMainDiagnosis) {
						this.componentEvaluationManagerService.mainDiagnosis = diagnosis;
						this.mainDiagnosis = { main: diagnosis };
						this.mainDiagnosisChange.emit(this.mainDiagnosis);
					}
					else {
						this.otherDiagnoses.push({ diagnosis });
						this.componentEvaluationManagerService.diagnosis = this.otherDiagnoses.map(diagnosis => diagnosis.diagnosis);
						this.diagnosisChange.emit(this.otherDiagnoses);
					}
				}
				else
					this.snackBarService.showError('internaciones.anamnesis.diagnosticos.messages.ERROR');
			}
		});
	}

	openModifyMainDiagnosisDialog() {
		const dialogRef = this.dialog.open(SelectMainDiagnosisComponent, {
			width: '450px',
			data: {
				currentMainDiagnosis: this.mainDiagnosis.main,
				otherDiagnoses: this.otherDiagnoses.filter(otherDiagnosis => otherDiagnosis.diagnosis.statusId === HEALTH_CLINICAL_STATUS.ACTIVO)
					.map(otherDiagnosis => otherDiagnosis.diagnosis)
			}
		});

		dialogRef.afterClosed().subscribe(potentialNewMainDiagnosis => {
			if (potentialNewMainDiagnosis) {
				if (potentialNewMainDiagnosis.snomed.sctid != this.mainDiagnosis.main.snomed.sctid) {
					this.componentEvaluationManagerService.mainDiagnosis = potentialNewMainDiagnosis;
					let oldMainDiagnosis = this.mainDiagnosis;
					this.otherDiagnoses.push({ diagnosis: oldMainDiagnosis.main, isAssociatedToIsolationAlert: oldMainDiagnosis.isAssociatedToIsolationAlert });
					const index = this.otherDiagnoses.findIndex((diagnosis: EmergencyCareDiagnosis) => diagnosis.diagnosis.snomed.sctid === potentialNewMainDiagnosis.snomed.sctid);
					this.mainDiagnosis.main = potentialNewMainDiagnosis;
					this.mainDiagnosis.main.isAdded = true;
					this.mainDiagnosis.isAssociatedToIsolationAlert = getElementAtPosition<EmergencyCareDiagnosis>(this.otherDiagnoses, index).isAssociatedToIsolationAlert;
					this.otherDiagnoses.splice(index, 1);
					this.mainDiagnosisChange.emit(this.mainDiagnosis);
					this.diagnosisChange.emit(this.otherDiagnoses);
				}
			}
		});
	}

	removeDiagnosis(removeDiagnosis: DiagnosisDto) {
		const index = this.otherDiagnoses.findIndex((diagnosis: EmergencyCareDiagnosis) => diagnosis.diagnosis.snomed.sctid === removeDiagnosis.snomed.sctid);
		if (index !== -1) {
			this.otherDiagnoses.splice(index, 1);
			this.componentEvaluationManagerService.diagnosis = this.otherDiagnoses.map(otherDiagnosis => otherDiagnosis.diagnosis);
			this.diagnosisChange.emit(this.otherDiagnoses);
		}
	}

	removeMainDiagnosis() {
		this.mainDiagnosis = { main: null };
		this.mainDiagnosisChange.emit(this.mainDiagnosis);
	}

	updateAll() {
		this.allChecked = this.otherDiagnoses?.every(otherDiagnosis => otherDiagnosis.diagnosis.isAdded);
		this.diagnosisChange.emit(this.otherDiagnoses);
	}

	someComplete(): boolean {
		if (this.otherDiagnoses == null) {
			return false;
		}
		return this.otherDiagnoses.filter(otherDiagnosis => otherDiagnosis.diagnosis.isAdded).length > 0 && !this.allChecked;
	}

	setAll(completed: boolean) {
		this.allChecked = completed;
		if (this.otherDiagnoses == null) {
			return;
		}
		this.otherDiagnoses.forEach(otherDiagnosis => {
			if (!otherDiagnosis.isAssociatedToIsolationAlert) {
				otherDiagnosis.diagnosis.isAdded = completed
			}
		});

		this.diagnosisChange.emit(this.otherDiagnoses);
	}

	updateDiagnosis(diagnosisToUpdate: DiagnosisDto) {
		const indexToUpdate = this.otherDiagnoses.findIndex(diagnosis => diagnosis.diagnosis.snomed.sctid === diagnosisToUpdate.snomed.sctid);

		if (indexToUpdate !== -1)
			this.otherDiagnoses[indexToUpdate].diagnosis = diagnosisToUpdate;

		this.diagnosisChange.next(this.otherDiagnoses);
	}

}

export interface EmergencyCareMainDiagnosis {
	main: HealthConditionDto;
	isAssociatedToIsolationAlert?: boolean;
}

export interface EmergencyCareDiagnosis {
	diagnosis: DiagnosisDto;
	isAssociatedToIsolationAlert?: boolean;
}