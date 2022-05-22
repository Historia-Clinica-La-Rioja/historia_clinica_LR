import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { DiagnosisCreationEditionComponent } from '../../dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { SelectMainDiagnosisComponent } from '../../dialogs/select-main-diagnosis/select-main-diagnosis.component';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent implements OnInit {

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

	constructor(
		public dialog: MatDialog,
		private snackBarService: SnackBarService
	) { }

	ngOnInit(): void {
	}

	openCreationDialog(isMainDiagnosis: boolean) {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION'
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				if (!this.diagnosticos.find(currentDiagnosis => currentDiagnosis.snomed.pt === diagnosis.snomed.pt) && diagnosis.snomed.pt!=this._mainDiagnosis?.snomed.pt)
					if (isMainDiagnosis){
						diagnosis.presumptive = false;
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
				otherDiagnoses: this.diagnosticos
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
					this._mainDiagnosis.verificationId = '59156000';
					(<DiagnosisDto>this._mainDiagnosis).presumptive = false;
				}
			}
		});
	}

}
