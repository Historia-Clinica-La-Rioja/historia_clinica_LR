import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { EVirtualConsultationPriority } from '@api-rest/api-model';


@Component({
	selector: 'app-priority-select',
	templateUrl: './priority-select.component.html',
	styleUrls: ['./priority-select.component.scss']
})
export class PrioritySelectComponent implements OnInit {

	@Output() selectionChange = new EventEmitter();
	priorities = Object.values(DescriptionPriority);
	PRIORITY = DescriptionPriority;

	constructor() { }

	ngOnInit(): void {

	}

	selectPriority(selected) {
		let priorization: EVirtualConsultationPriority;
		switch (selected) {
			case this.PRIORITY.HIGH:
				priorization = EVirtualConsultationPriority.HIGH
				break;
			case this.PRIORITY.MEIDUM:
				priorization = EVirtualConsultationPriority.MEDIUM
				break;
			case this.PRIORITY.LOW:
				priorization = EVirtualConsultationPriority.LOW
				break;

		}
		this.selectionChange.emit(priorization);
	}
}
export enum DescriptionPriority {
	LOW = 'Baja prioridad',
	MEIDUM = 'Media Prioridad',
	HIGH = 'Alta prioridad',
}

