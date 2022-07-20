import { HEALTH_CLINICAL_STATUS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HEALTH_VERIFICATIONS } from '../../constants/ids';
import { DiagnosisCreationEditionComponent } from '../../dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from '../../dialogs/select-main-diagnosis/select-main-diagnosis.component';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent {

	@Output() diagnosisChange = new EventEmitter();
	@Output() mainDiagnosisChange = new EventEmitter();

	@Input()
	diagnosticos: DiagnosisDto[];
	_mainDiagnosis: HealthConditionDto;

	@Input()
	set mainDiagnosis(newMainDiagnosis: HealthConditionDto) {
		this._mainDiagnosis = newMainDiagnosis;
		this.mainDiagnosisChange.emit(this._mainDiagnosis)
	}

	@Input()
	type: string;

	CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
	ACTIVE = HEALTH_CLINICAL_STATUS.ACTIVO;

	constructor(
		public dialog: MatDialog,
		private snackBarService: SnackBarService
	) { }

	openCreationDialog(isMainDiagnosis: boolean) {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: isMainDiagnosis
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				if (!this.diagnosticos.find(currentDiagnosis => currentDiagnosis.snomed.pt === diagnosis.snomed.pt) && diagnosis.snomed.pt != this._mainDiagnosis?.snomed.pt)
					if (isMainDiagnosis) {
						diagnosis.presumptive = false;
						diagnosis.verificationId = this.CONFIRMED;
						diagnosis.statusId = this.ACTIVE;
						this.mainDiagnosis = diagnosis;
					}
					else {
						this.diagnosticos.push(diagnosis);
						this.diagnosisChange.emit(this.diagnosticos);
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
				currentMainDiagnosis: this._mainDiagnosis,
				otherDiagnoses: this.diagnosticos.filter(d => d.statusId === this.ACTIVE)
			}
		});

		dialogRef.afterClosed().subscribe(potentialNewMainDiagnosis => {
			if (potentialNewMainDiagnosis) {
				if (potentialNewMainDiagnosis != this._mainDiagnosis) {
					let oldMainDiagnosis = this._mainDiagnosis;
					this.diagnosticos.push(oldMainDiagnosis);
					this.diagnosticos.splice(this.diagnosticos.indexOf(potentialNewMainDiagnosis), 1);
					this.mainDiagnosis = potentialNewMainDiagnosis;
					this._mainDiagnosis.isAdded = true;
					this._mainDiagnosis.verificationId = this.CONFIRMED;
					this._mainDiagnosis.statusId = this.ACTIVE;
					(<DiagnosisDto>this._mainDiagnosis).presumptive = false;
				}
			}
		});
	}

}
