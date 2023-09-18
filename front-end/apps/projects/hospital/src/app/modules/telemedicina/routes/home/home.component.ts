import { Component, OnInit } from '@angular/core';
import { EVirtualConsultationPriority } from '@api-rest/api-model';
import { mapPriority } from '../../virtualConsultations.utils';
import { Option } from '@presentation/components/filters-select/filters-select.component';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { StompService } from 'projects/hospital/src/app/stomp.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
	priorityOptions: Option[] = [];
	availitibyOptions: Option[] = [];
	constructor(public virtualConsultationsFacadeService: VirtualConsultationsFacadeService,	private virtualConsultationService: VirtualConstultationService,
		private readonly stompService: StompService,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,) {
		this.setPriotityOptionsFilter();
		this.setAvailabilityOptionsFilter();
	}

	ngOnInit(): void {
		this.virtualConsultationsFacadeService = new VirtualConsultationsFacadeService(this.virtualConsultationService,this.stompService,this.contextService,this.permissionsService);
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
		let priority:Option = {
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
}
