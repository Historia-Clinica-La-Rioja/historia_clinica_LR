import {TranslateService} from '@ngx-translate/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Component, Inject, OnInit} from '@angular/core';
import {CreateOutpatientDto} from '@api-rest/api-model';

@Component({
	selector: 'app-confirmar-nueva-consulta-popup',
	templateUrl: './confirmar-nueva-consulta-popup.component.html',
	styleUrls: ['./confirmar-nueva-consulta-popup.component.scss']
})
export class ConfirmarNuevaConsultaPopupComponent implements OnInit {
	public elementosFaltantes: string[];
	public elementosPresentes: string[];

	constructor(
		private readonly translateService: TranslateService,
		public dialogRef: MatDialogRef<ConfirmarNuevaConsultaPopupComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {	nuevaConsulta: CreateOutpatientDto }
	) {
		this.elementosFaltantes = [];
		this.elementosPresentes = [];
	}

	addTranslated(elemento: string, lista: string[]) {
		this.translateService.get(elemento).subscribe( traduccion =>
			lista.push(traduccion)
		);
	}

	fillLists() {

		this.data.nuevaConsulta.reasons?.length ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.MOTIVO', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.MOTIVO', this.elementosFaltantes);

		this.data.nuevaConsulta.problems?.length ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PROBLEMA', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PROBLEMA', this.elementosFaltantes);
		this.data.nuevaConsulta.anthropometricData.weight ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.elementosFaltantes);

		this.data.nuevaConsulta.anthropometricData.height ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.elementosFaltantes);

		this.data.nuevaConsulta.vitalSigns.systolicBloodPressure ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.elementosFaltantes);

		this.data.nuevaConsulta.vitalSigns.diastolicBloodPressure ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.elementosPresentes) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.elementosFaltantes);
	}

	ngOnInit(): void {
		this.fillLists();
	}
}

