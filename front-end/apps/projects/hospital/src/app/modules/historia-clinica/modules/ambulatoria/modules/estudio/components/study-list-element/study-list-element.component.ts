import { Component, Input, OnInit } from '@angular/core';
import { Content } from '@presentation/components/indication/indication.component';

@Component({
	selector: 'app-study-list-element',
	templateUrl: './study-list-element.component.html',
	styleUrls: ['./study-list-element.component.scss']
})
export class StudyListElementComponent implements OnInit {

	@Input() content: Content;
	@Input() createdDuring: CreatedDuring;


	CreatedDuring = CreatedDuring;
	constructor() { }

	ngOnInit(): void {
	}

}

export enum CreatedDuring {
	INTERNMENT, EMERGENCY_CARE
}
