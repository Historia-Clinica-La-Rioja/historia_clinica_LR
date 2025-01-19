import { Component, OnDestroy, OnInit } from '@angular/core';
import { EVirtualConsultationPriority, VirtualConsultationNotificationDataDto } from '@api-rest/api-model';
import { mapPriority } from '../../virtualConsultations.utils';
import { Option } from '@presentation/components/filters-select/filters-select.component';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { EntryCallStompService } from '../../../api-web-socket/entry-call-stomp.service';
import { ShowEntryCallService } from '../../show-entry-call.service';
import { Subscription } from 'rxjs';
import { VirtualConsultationStompService } from '../../../api-web-socket/virtual-consultation-stomp.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
	priorityOptions: Option[] = [];
	availitibyOptions: Option[] = [];

	private entryCallSubs: Subscription;

	constructor(
		public virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private entryCallStompService: EntryCallStompService,
		private showEntryCallService: ShowEntryCallService,
		private readonly virtualConsultationStompService: VirtualConsultationStompService,
	) {
		this.setPriotityOptionsFilter();
		this.setAvailabilityOptionsFilter();
	}

	ngOnInit(): void {
		this.virtualConsultationsFacadeService = new VirtualConsultationsFacadeService(this.virtualConsultationService, this.contextService, this.permissionsService,this.virtualConsultationStompService);
		this.entryCallSubs = this.entryCallStompService.entryCall$.subscribe(
			(call: VirtualConsultationNotificationDataDto) => {
				this.showEntryCallService.show(call);
			}
		)
	}
	setAvailabilityOptionsFilter() {
		let option: Option = {
			id: true,
			description: "Disponible"
		}
		this.availitibyOptions.push(option);

		let otherOption: Option = {
			id: false,
			description: "No disponible"
		}
		this.availitibyOptions.push(otherOption);
	}

	setPriotityOptionsFilter() {
		let priority: Option = {
			id: EVirtualConsultationPriority.HIGH,
			description: mapPriority[EVirtualConsultationPriority.HIGH],
		}
		this.priorityOptions.push(priority);
		priority = {
			id: EVirtualConsultationPriority.LOW,
			description: mapPriority[EVirtualConsultationPriority.LOW],
		}
		this.priorityOptions.push(priority);
		priority = {
			id: EVirtualConsultationPriority.MEDIUM,
			description: mapPriority[EVirtualConsultationPriority.MEDIUM],
		}
		this.priorityOptions.push(priority);
	}

	ngOnDestroy(): void {
		this.entryCallSubs.unsubscribe();
	}
}
