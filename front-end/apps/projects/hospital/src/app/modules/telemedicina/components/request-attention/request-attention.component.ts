import { Component, OnInit } from '@angular/core';
import { EVirtualConsultationStatus, VirtualConsultationDto } from '@api-rest/api-model';
import { mapPriority, statusLabel } from '../../virtualConsultations.utils';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { Subscription } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';

@Component({
	selector: 'app-request-attention',
	templateUrl: './request-attention.component.html',
	styleUrls: ['./request-attention.component.scss']
})
export class RequestAttentionComponent implements OnInit {

	virtualConsultationsSubscription: Subscription;
	virtualConsultations: any[] = [];
	toggleEnabled = false;

	constructor(
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private jitsiCallService: JitsiCallService,
	) { }


	ngOnInit(): void {

		this.virtualConsultationsSubscription = this.virtualConsultationsFacadeService.virtualConsultations$
			.subscribe(virtualConsultations =>
				this.virtualConsultations = virtualConsultations.map(this.toVCToBeShown))
	}

	confirm(virtualConsultationId: number) {
		this.virtualConsultationService.changeVirtualConsultationState(virtualConsultationId, { status: EVirtualConsultationStatus.FINISHED }).subscribe()
	}


	joinMeet(virtualConsultation) {
		this.virtualConsultationService.notifyVirtualConsultationCall(virtualConsultation.id).subscribe(
			_ => {
				this.jitsiCallService.open(virtualConsultation.callId);
			}
		)
	}

	availabilityChanged(availability: boolean) {
		this.virtualConsultationService.changeClinicalProfessionalAvailability(availability).subscribe();
		this.toggleEnabled = availability;
	}

	private toVCToBeShown(vc: VirtualConsultationDto) {
		return {
			...vc,
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime))
		}
	}

}
