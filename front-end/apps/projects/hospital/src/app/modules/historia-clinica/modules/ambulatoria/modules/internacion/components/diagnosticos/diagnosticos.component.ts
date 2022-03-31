import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { DiagnosisCreationEditionComponent } from '../../dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';

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
	set mainDiagnosis(newMainDiagnosis: HealthConditionDto){
		this._mainDiagnosis = newMainDiagnosis;
		this.mainDiagnosisChange.emit(this._mainDiagnosis)
	}

	@Input()
	type: string;

	constructor(public dialog: MatDialog) {}

	ngOnInit(): void {
	}

	openCreationDialog(isMainDiagnosis: boolean) {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			height: '270px',
			data: {
				type: 'CREATION'
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				if (isMainDiagnosis)
					this.mainDiagnosis = diagnosis;
				else {
					this.diagnosticos.push(diagnosis);
					this.diagnosisChange.emit(this.diagnosticos);
				}
			}
		});
	}

}
