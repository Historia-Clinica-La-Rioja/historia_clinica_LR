import { Component, OnInit, Input } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from 'src/app/modules/presentation/components/detail-box/detail-box.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { AnthropometricDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input()
	internmentEpisodeId: number;
	antropometricosSummary = ANTROPOMETRICOS;

	details: DetailBox[] = [];

	constructor(
		private internmentStateService: InternmentStateService
	) { }

	ngOnInit(): void {
		const LABELS = {
			bloodType: 'Grupo sanguíneo',
			height: 'Talla (cm)',
			weight: 'Peso (kg)',
			bmi: 'IMC',
		};
		this.internmentStateService.getAnthropometricData(this.internmentEpisodeId).subscribe(
			(anthropometricData: AnthropometricDataDto) => {
				if (anthropometricData) {
					Object.keys(anthropometricData).forEach(key => this.details.push(
							{
								description: LABELS[key],
								value: anthropometricData[key]?.value
							}
						));
				}
			}
		);
	}

}
