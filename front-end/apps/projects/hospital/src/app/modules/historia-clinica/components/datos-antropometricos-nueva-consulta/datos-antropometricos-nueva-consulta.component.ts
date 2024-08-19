import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { DatosAntropometricosNuevaConsultaService } from '../../modules/ambulatoria/services/datos-antropometricos-nueva-consulta.service';
import { BoxMessageInformation } from '../box-message/box-message.component';
import { TranslateService } from '@ngx-translate/core';
import { AnthropometricData, PatientEvolutionChartsService } from '@historia-clinica/services/patient-evolution-charts.service';
import { Observable } from 'rxjs/internal/Observable';

@Component({
	selector: 'app-datos-antropometricos-nueva-consulta',
	templateUrl: './datos-antropometricos-nueva-consulta.component.html',
	styleUrls: ['./datos-antropometricos-nueva-consulta.component.scss'],
	providers: [PatientEvolutionChartsService]
})
export class DatosAntropometricosNuevaConsultaComponent implements OnInit, OnChanges {

	readonly isNumberOrDot = isNumberOrDot;
	boxMessageInfo: BoxMessageInformation;
	anthropometricData$: Observable<AnthropometricData>;

	@Input() datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	@Input() showPreloadData: boolean = false;
	@Input() patientId: number;

	constructor(
		private readonly translateService: TranslateService
	) { }

	ngOnInit(): void {
		this.anthropometricData$ = this.datosAntropometricosNuevaConsultaService.antropometricData$;
	}

	ngOnChanges() {
		this.boxMessageInfo = {
			title: 'historia-clinica.include-previous-data-question.TITLE',
			question: 'historia-clinica.include-previous-data-question.QUESTION',
			message: this.translateService.instant('historia-clinica.include-previous-data-question.DESCRIPTION',
				{
					dataName: this.translateService.instant('ambulatoria.paciente.nueva-consulta.datos-antropometricos.NAME'),
					date: this.datosAntropometricosNuevaConsultaService.getDate()
				}),
			showButtons: true
		}
	}

	savePreloadData(save: boolean): void {
		if (save) {
			this.datosAntropometricosNuevaConsultaService.savePreloadedAnthropometricData()
		}
		else {
			this.datosAntropometricosNuevaConsultaService.discardPreloadedAnthropometricData();
		}
	}

}
