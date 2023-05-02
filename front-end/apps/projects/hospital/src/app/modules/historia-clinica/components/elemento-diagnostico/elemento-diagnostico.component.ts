import { HEALTH_CLINICAL_STATUS, HEALTH_VERIFICATIONS } from '../../modules/ambulatoria/modules/internacion/constants/ids';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto } from '@api-rest/api-model';
import { DiagnosisCreationEditionComponent } from '../../modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';

@Component({
	selector: 'app-elemento-diagnostico',
	templateUrl: './elemento-diagnostico.component.html',
	styleUrls: ['./elemento-diagnostico.component.scss']
})
export class ElementoDiagnosticoComponent {

	@Input()
	diagnosis: DiagnosisDto;

	@Input()
	isMain: boolean;

	@Output() removeDiagnosis = new EventEmitter();

	ACTIVE = HEALTH_CLINICAL_STATUS.ACTIVO;
	REMISSION = HEALTH_CLINICAL_STATUS.REMISION;
	RESOLVED = HEALTH_CLINICAL_STATUS.RESUELTO;
	CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
	DISCARDED = HEALTH_VERIFICATIONS.DESCARTADO;

	constructor(public dialog: MatDialog) { }

	openDiagnosisEditionDialog() {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'EDITION',
				diagnosis: this.diagnosis
			}
		});
	}

	remove(){
		this.removeDiagnosis.emit(this.diagnosis)
	}

}
