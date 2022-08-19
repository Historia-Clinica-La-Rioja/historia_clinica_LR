import { EIndicationType } from '@api-rest/api-model';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-indication',
	templateUrl: './indication.component.html',
	styleUrls: ['./indication.component.scss']
})
export class IndicationComponent {

	@Input() header: Title;
	@Input() contents: Content[];
	@Input() noInformation: string;
	@Input() menuOption: string;
	@Output() onMenuOptionClick = new EventEmitter<Content>();

	constructor() { }

	menuOptionClick(content: Content): void{
		this.onMenuOptionClick.emit(content);
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

