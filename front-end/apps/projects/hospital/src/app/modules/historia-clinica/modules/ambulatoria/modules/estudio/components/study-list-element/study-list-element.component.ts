import { Component, Input, OnInit } from '@angular/core';
import { ContentStudy } from '../study/study.component';

@Component({
	selector: 'app-study-list-element',
	templateUrl: './study-list-element.component.html',
	styleUrls: ['./study-list-element.component.scss']
})
export class StudyListElementComponent implements OnInit {

	@Input() content: ContentStudy;
	@Input() createdDuring: CreatedDuring;


	CreatedDuring = CreatedDuring;
	constructor() { }

	ngOnInit(): void {
	}

}

export enum CreatedDuring {
	INTERNMENT, EMERGENCY_CARE
}
