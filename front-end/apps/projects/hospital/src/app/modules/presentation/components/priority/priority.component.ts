import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-priority',
	templateUrl: './priority.component.html',
	styleUrls: ['./priority.component.scss']
})
export class PriorityComponent {

	@Input() set priority(_priority: Priority) {
		this.priorityDescription = _priority
		this.priorityClass = `${_priority}-text`;
		this.priorityCircleClass = `${_priority} circle`;
	}

	priorityClass;
	priorityDescription;
	priorityCircleClass;

	constructor() { }

}

export enum Priority {
	HIGH = 'alta',
	MEDIUM = 'media',
	LOW = 'baja'
}
