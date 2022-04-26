import { Component, Input, OnInit } from '@angular/core';
import { Content } from '../indication/indication.component';

@Component({
	selector: 'app-list-element-item',
	templateUrl: './list-element-item.component.html',
	styleUrls: ['./list-element-item.component.scss']
})
export class ListElementItemComponent implements OnInit {

	@Input() content: Content;

	constructor() { }

	ngOnInit(): void {
	}

}
