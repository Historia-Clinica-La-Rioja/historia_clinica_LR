import { Component, Input } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-pharmarco-detail',
	templateUrl: './pharmarco-detail.component.html',
	styleUrls: ['./pharmarco-detail.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class PharmarcoDetailComponent {

	@Input() pharmaco: PharmarcoDetail;
}

export interface PharmarcoDetail {
	pt: string,
	unitDose: number,
	dayDose: number,
	treatmentDays: number,
	quantity: number
}