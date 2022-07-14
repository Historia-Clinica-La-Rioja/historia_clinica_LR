import { EIndicationType } from '@api-rest/api-model';
import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-indication',
	templateUrl: './indication.component.html',
	styleUrls: ['./indication.component.scss']
})
export class IndicationComponent {

	@Input() header: Title;
	@Input() contents: Content[];
	@Input() noInformation: string;

	constructor() { }

}

export interface Title {
	title: string;
	matIcon?: string;
	svgIcon?: string;
}

export interface Content {
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

