import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewTelemedicineRequestComponent } from '../../dialogs/new-telemedicine-request/new-telemedicine-request.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-requests',
	templateUrl: './requests.component.html',
	styleUrls: ['./requests.component.scss']
})
export class RequestsComponent {

	virtualConsultations: any[] = [];
	initialResponsableStatus = false;

	constructor(
		private dialog: MatDialog,
		private virtualConsultationService: VirtualConstultationService,
		private contextService: ContextService
	) {
		this.virtualConsultationService.getResponsibleStatus(this.contextService.institutionId).subscribe(
			status => this.initialResponsableStatus = status
		)
	 }


	openAddRequest() {
		this.dialog.open(NewTelemedicineRequestComponent, {
			disableClose: true,
			width: '700px',
			height: '700px',
		})
	}

	availabilityChanged(available: boolean) {
		this.virtualConsultationService.changeResponsibleAttentionState(this.contextService.institutionId, available).subscribe();
	}

}

