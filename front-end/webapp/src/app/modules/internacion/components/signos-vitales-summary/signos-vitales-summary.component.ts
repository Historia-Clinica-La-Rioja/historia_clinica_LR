import { Component, OnInit, Input } from '@angular/core';
import { SummaryHeader } from 'src/app/modules/presentation/components/summary-card/summary-card.component';
import { SIGNOS_VITALES } from '../../constants/summaries';
import { VitalSingCurrentPrevious } from 'src/app/modules/presentation/components/signo-vital-current-previous/signo-vital-current-previous.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { VitalSignDto } from '@api-rest/api-model';

@Component({
	selector: 'app-signos-vitales-summary',
	templateUrl: './signos-vitales-summary.component.html',
	styleUrls: ['./signos-vitales-summary.component.scss']
})
export class SignosVitalesSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	signosVitalesSummary: SummaryHeader = SIGNOS_VITALES;
	signosVitales: VitalSingCurrentPrevious[] = [];

	constructor(
		private internmentStateService: InternmentStateService,
	) { }

	ngOnInit(): void {
		const LABELS = {
			systolicBloodPressure: 'Tensión arterial sistólica',
			diastolicBloodPressure: 'Tensión arterial diastólica',
			heartRate: 'Frecuencia cardíaca',
			respiratoryRate: 'Frecuencia respiratoria',
			temperature: 'Temperatura',
			bloodOxygenSaturation: 'Saturación de oxígeno',
		};
		this.internmentStateService.getVitalSigns(this.internmentEpisodeId).subscribe(
			(vitalSigns: VitalSignDto[]) => {
				let current = vitalSigns[0] || {};
				let previous = vitalSigns[1] || {};
				Object.keys(LABELS).forEach(key => this.signosVitales.push(
					{
						description: LABELS[key],
						currentValue: Number(current[key]?.value),
						previousValue: Number(previous[key]?.value)
					}
				));
			}
		);
	}

}
