import { Component, Input } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-pharmaco-detail',
	templateUrl: './pharmaco-detail.component.html',
	styleUrls: ['./pharmaco-detail.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class PharmacoDetailComponent {

	@Input() pharmaco: PharmacoDetail;
}

export interface PharmacoDetail {
	pt: string,
	unitDose: number,
	dayDose: number,
	treatmentDays: number,
	quantity: number
}
