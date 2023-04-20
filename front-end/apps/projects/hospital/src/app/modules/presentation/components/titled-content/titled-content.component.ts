import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-titled-content',
	templateUrl: './titled-content.component.html',
	styleUrls: ['./titled-content.component.scss']
})
export class TitledContentComponent implements OnInit {

	@Input() icon: string;
	@Input() title: string;
	@Input() content: string[];
	constructor() { }

	ngOnInit(): void {
	}

}
