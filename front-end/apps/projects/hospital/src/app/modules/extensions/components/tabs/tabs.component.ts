import { Component, Input } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-tabs',
	templateUrl: './tabs.component.html',
	styleUrls: ['./tabs.component.scss']
})
export class TabsComponent {
	@Input() tabs: UIComponentDto[];

	constructor() { }

}
