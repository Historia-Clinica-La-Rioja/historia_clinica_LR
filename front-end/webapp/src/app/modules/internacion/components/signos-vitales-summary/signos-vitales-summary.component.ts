import { Component, OnInit, Input } from '@angular/core';
import { SummaryHeader } from 'src/app/modules/presentation/components/summary-card/summary-card.component';
import { SIGNOS_VITALES } from '../../constants/summaries';
import { VitalSingCurrentPrevious } from 'src/app/modules/presentation/components/signo-vital-current-previous/signo-vital-current-previous.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { VitalSignDto, Last2VitalSignsDto } from '@api-rest/api-model';
import { momentParseDate } from '@core/utils/moment.utils';

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
		this.internmentStateService.getVitalSigns(this.internmentEpisodeId).subscribe(
			this.initSignosVitales(), this.initSignosVitales()
		);
	}

	initSignosVitales(): (vitalSigns: Last2VitalSignsDto) => void {
		const LABELS = {
			systolicBloodPressure: 'Tensión arterial sistólica',
			diastolicBloodPressure: 'Tensión arterial diastólica',
			heartRate: 'Frecuencia cardíaca',
			respiratoryRate: 'Frecuencia respiratoria',
			temperature: 'Temperatura',
			bloodOxygenSaturation: 'Saturación de oxígeno',
		};
		return (vitalSigns: Last2VitalSignsDto) => {
			let current: VitalSignDto = vitalSigns.current || {};
			let previous: VitalSignDto = vitalSigns.previous || {};
			Object.keys(LABELS).forEach(key => this.signosVitales.push(
				{
					description: LABELS[key],
					currentValue: {
						value: Number(current[key]?.value),
						effectiveTime: current[key]?.effectiveTime ? momentParseDate(current[key].effectiveTime) : undefined
					},
					previousValue: {
						value: Number(previous[key]?.value),
						effectiveTime: previous[key]?.effectiveTime ? momentParseDate(previous[key].effectiveTime) : undefined
					}
				}
			));
		}
	}

}
