import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { HEALTH_CLINICAL_STATUS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HEALTH_VERIFICATIONS } from '../../modules/ambulatoria/modules/internacion/constants/ids';
import { DiagnosisCreationEditionComponent, DiagnosisMode } from '../../modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from '../../modules/ambulatoria/modules/internacion/dialogs/select-main-diagnosis/select-main-diagnosis.component';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent {

	allChecked = false;
	diagnosticos: DiagnosisDto[];
	@Input() showTitle = false;


	@Input() set listDiagnosis(diagnosticos: DiagnosisDto[]) {
		this.diagnosticos = diagnosticos;
		this.allChecked = diagnosticos?.every(t => t.isAdded);
	};
	@Input() mainDiagnosis: HealthConditionDto;
	@Input() type: string;
	@Output() diagnosisChange = new EventEmitter();
	@Output() mainDiagnosisChange = new EventEmitter();

	CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
	ACTIVE = HEALTH_CLINICAL_STATUS.ACTIVO;

	constructor(
		public dialog: MatDialog,
		private snackBarService: SnackBarService,
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,

	) { }

	private isMainDiagnosis(diagnosis: DiagnosisDto): boolean {
		return !this.diagnosticos.find(currentDiagnosis => currentDiagnosis.snomed.pt === diagnosis.snomed.pt) && diagnosis.snomed.pt != this.mainDiagnosis?.snomed.pt
	}
	openCreationDialog(isMainDiagnosis: boolean) {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				diagnosisMode: DiagnosisMode.CREATION,
				isMainDiagnosis: isMainDiagnosis
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				if (this.isMainDiagnosis(diagnosis)) {

					if (isMainDiagnosis) {
						this.componentEvaluationManagerService.mainDiagnosis = diagnosis;
						diagnosis.presumptive = false;
						diagnosis.verificationId = this.CONFIRMED;
						diagnosis.statusId = this.ACTIVE;
						this.mainDiagnosis = diagnosis;
						this.mainDiagnosisChange.emit(this.mainDiagnosis);
					}
					else {
						this.diagnosticos.push(diagnosis);
						this.componentEvaluationManagerService.diagnosis = this.diagnosticos;
						this.diagnosisChange.emit(this.diagnosticos);
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
				currentMainDiagnosis: this.mainDiagnosis,
				otherDiagnoses: this.diagnosticos.filter(d => d.statusId === this.ACTIVE)
			}
		});

		dialogRef.afterClosed().subscribe(potentialNewMainDiagnosis => {
			if (potentialNewMainDiagnosis) {
				if (potentialNewMainDiagnosis != this.mainDiagnosis) {
					this.componentEvaluationManagerService.mainDiagnosis = potentialNewMainDiagnosis;
					let oldMainDiagnosis = this.mainDiagnosis;
					this.diagnosticos.push(oldMainDiagnosis);
					this.diagnosticos.splice(this.diagnosticos.indexOf(potentialNewMainDiagnosis), 1);
					this.mainDiagnosis = potentialNewMainDiagnosis;
					this.mainDiagnosis.isAdded = true;
					this.mainDiagnosis.verificationId = this.CONFIRMED;
					this.mainDiagnosis.statusId = this.ACTIVE;
					(<DiagnosisDto>this.mainDiagnosis).presumptive = false;
					this.mainDiagnosisChange.emit(this.mainDiagnosis);

				}
			}
		});
	}

	removeDiagnosis(event: any) {
		const index = this.diagnosticos.indexOf(event);
		if (index !== -1) {
			this.diagnosticos.splice(index, 1);
			this.componentEvaluationManagerService.diagnosis = this.diagnosticos;
		}
	}

	removeMainDiagnosis() {
		this.mainDiagnosis = null;
		this.mainDiagnosisChange.emit(this.mainDiagnosis);
	}

	updateAll() {
		this.allChecked = this.diagnosticos?.every(t => t.isAdded);
	}

	someComplete(): boolean {
		if (this.diagnosticos == null) {
			return false;
		}
		return this.diagnosticos.filter(t => t.isAdded).length > 0 && !this.allChecked;
	}

	setAll(completed: boolean) {
		this.allChecked = completed;
		if (this.diagnosticos == null) {
			return;
		}
		this.diagnosticos.forEach(t => (t.isAdded = completed));
	}
}
