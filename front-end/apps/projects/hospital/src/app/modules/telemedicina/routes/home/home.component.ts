import { Component, OnInit } from '@angular/core';
import { EVirtualConsultationPriority } from '@api-rest/api-model';
import { mapPriority } from '../../virtualConsultations.utils';
import { Option } from '@presentation/components/filters-select/filters-select.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	priorityOptions: Option[] = [];
	availitibyOptions: Option[] = [];
	constructor() {
		this.setPriotityOptionsFilter();
		this.setAvailabilityOptionsFilter();
	}

	ngOnInit(): void {
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
