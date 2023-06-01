import { Component, Input, OnChanges } from '@angular/core';
import { PatientType } from '@api-rest/api-model';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';

@Component({
	selector: 'app-colored-div-patient-state',
	templateUrl: './colored-div-patient-state.component.html',
	styleUrls: ['./colored-div-patient-state.component.scss']
})
export class ColoredDivPatientStateComponent implements OnChanges {
	@Input() patientTypeId: number;
	background: string;
	color: string;
	patientsTypes: PatientType[];

	constructor(private readonly patientMasterDataService: PatientMasterDataService,) {
		this.patientMasterDataService.getTypesPatient().subscribe((patientsTypes: PatientType[]) => {
			this.patientsTypes = patientsTypes;
		})
	}

	ngOnChanges(): void {
		this.background = colorsById[this.patientTypeId]?.background;
		this.color= colorsById[this.patientTypeId]?.color;
	}
	getPatientType(value: number) {
		return this.patientsTypes?.find(type => type.id === value).description
	}

}
enum PatientTypeBackground {
	VALIDADO = '#CCEBE1',
	TEMPORARIO = '#DCF4FB',
	RECHAZADO = '#F2F2F2',
	PERMANENTE_NO_VALIDO = '#FFEED9',
	PERMANENTE= '#EEDAF2',
}
enum PatientTypeColors {
	VALIDADO = '#009B68',
	TEMPORARIO = '#00B2FF',
	RECHAZADO = '#A8A8A8',
	PERMANENTE_NO_VALIDO = '#FF8A00',
	PERMANENTE= '#AB47BC',
}

const colorsById = {
	1: { background: PatientTypeBackground.PERMANENTE, color: PatientTypeColors.PERMANENTE },
	2: { background: PatientTypeBackground.VALIDADO, color: PatientTypeColors.VALIDADO },
	3: { background: PatientTypeBackground.TEMPORARIO,color: PatientTypeColors.TEMPORARIO },
	6: { background: PatientTypeBackground.RECHAZADO,color: PatientTypeColors.RECHAZADO },
	7: { background: PatientTypeBackground.PERMANENTE_NO_VALIDO,color: PatientTypeColors.PERMANENTE_NO_VALIDO },
};


