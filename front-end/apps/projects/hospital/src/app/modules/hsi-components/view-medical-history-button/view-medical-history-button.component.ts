import { Component, Input } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { PresentationModule } from '@presentation/presentation.module';

export interface ViewMedicalHistory {
	title: string,
	rightIcon: string,
	routerLink: string
}

@Component({
 	selector: 'app-view-medical-history-button',
  	templateUrl: './view-medical-history-button.component.html',
  	styleUrls: ['./view-medical-history-button.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class ViewMedicalHistoryButtonComponent {
	@Input() set patientId(patientId: number) {
		this.data = {
			title: 'view-medical-history-button.TITLE',
			rightIcon: 'open_in_new',
			routerLink: `/institucion/${this.contextService.institutionId}/ambulatoria/paciente/${patientId}`,
		}
	};
	data: ViewMedicalHistory;

  	constructor(private readonly contextService: ContextService) {}
}