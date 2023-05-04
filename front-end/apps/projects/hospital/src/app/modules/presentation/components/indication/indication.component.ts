import { EIndicationType, ERole } from '@api-rest/api-model';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
	selector: 'app-indication',
	templateUrl: './indication.component.html',
	styleUrls: ['./indication.component.scss']
})
export class IndicationComponent {
	@Input() buttonIndication: EIndicationType;
	@Input() canEdit: ERole[] = [];
	@Input() header: Title;
	@Input() contents: Content[];
	@Input() noInformation: string;
	@Input() menuOption: string;
	@Output() onMenuOptionClick = new EventEmitter<Content>();
	@Output() actioned: Subject<EIndicationType> = new Subject();

	constructor() { }

	menuOptionClick(content: Content): void {
		this.onMenuOptionClick.emit(content);
	}

	action(event) {
		this.actioned.next(event);
	}
}

export interface Title {
	title: string;
	matIcon?: string;
	svgIcon?: string;
}

export interface Content {
	id?: number;
	status: Status;
	description: string;
	extra_info?: ExtraInfo[];
	createdBy: string;
	timeElapsed: string;
	observations?: string;
}

export interface Status {
	description: string;
	cssClass: string;
	type?: EIndicationType;
}

export interface ExtraInfo {
	title: string;
	content?: string;
}

