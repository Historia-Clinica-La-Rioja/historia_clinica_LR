import { Component, Input, OnInit } from '@angular/core';
import { Content } from '@presentation/components/indication/indication.component';

@Component({
	selector: 'app-study-list-element',
	templateUrl: './study-list-element.component.html',
	styleUrls: ['./study-list-element.component.scss']
})
export class StudyListElementComponent implements OnInit {

	@Input() content: Content;
	@Input() wasCreatedDuringInternment: boolean;

	constructor() { }

	ngOnInit(): void {
	}

}
