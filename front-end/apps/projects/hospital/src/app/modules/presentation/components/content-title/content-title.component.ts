import { Component, Input } from '@angular/core';
import { Label } from '../label/label.component';

export interface Title {
	icon?: string;
	label: Label;
};

@Component({
	selector: 'app-content-title',
	templateUrl: './content-title.component.html',
	styleUrls: ['./content-title.component.scss']
})
export class ContentTitleComponent {
	@Input() title: Title;
	@Input() collapsed = false;

	constructor() { }

}
