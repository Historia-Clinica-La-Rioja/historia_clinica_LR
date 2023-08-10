import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewTelemedicineRequestComponent } from '../../dialogs/new-telemedicine-request/new-telemedicine-request.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { VirtualConsultationsFacadeService } from 'projects/hospital/src/app/modules/telemedicina/virtual-consultations-facade.service';
import { EVirtualConsultationPriority, EVirtualConsultationStatus, VirtualConsultationDto } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { Priority } from '@presentation/components/priority/priority.component';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-requests',
	templateUrl: './requests.component.html',
	styleUrls: ['./requests.component.scss']
})
export class RequestsComponent implements OnInit {

	virtualConsultations: any[] = [];

	constructor(
		private dialog: MatDialog,
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private jitsiCallService: JitsiCallService,
		private contextService: ContextService
	) { }

	ngOnInit(): void {

		this.virtualConsultationsFacadeService.virtualConsultations$
			.subscribe(virtualConsultations =>
				this.virtualConsultations = virtualConsultations.map(this.toVCToBeShown))
	}

	joinMeet(virtualConsultation) {
		this.virtualConsultationService.notifyVirtualConsultationCall(virtualConsultation.id).subscribe(
			_ => {
				this.jitsiCallService.open(virtualConsultation.callId);
			}
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

	/* cambioEstadoDeLaSolicitud() {
		this.virtualConsultationsFacadeService.cambioEstadoDeLaSolicitud()
	} */

	private toVCToBeShown(vc: VirtualConsultationDto) {
		return {
			...vc,
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtoToDate(vc.creationDateTime))
		}
	}
}

const statusLabel = {
	[EVirtualConsultationStatus.FINISHED]: {
		description: 'Finalizada',
		color: Color.GREEN
	},
	[EVirtualConsultationStatus.IN_PROGRESS]: {
		description: 'En Progreso',
		color: Color.BLUE
	},
	[EVirtualConsultationStatus.PENDING]: {
		description: 'Pendiente',
		color: Color.YELLOW
	}
}

const mapPriority = {
	[EVirtualConsultationPriority.HIGH]: Priority.HIGH,
	[EVirtualConsultationPriority.MEDIUM]: Priority.MEDIUM,
	[EVirtualConsultationPriority.LOW]: Priority.LOW,

}
