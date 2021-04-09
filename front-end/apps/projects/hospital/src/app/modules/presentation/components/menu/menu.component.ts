import { Component, Input } from '@angular/core';
import { Label } from '../label/label.component';

export interface MenuItem {
	label: Label;
	icon: string;
	url: string;
	id: string;
	options?: any;
}

export const defToMenuItem = ({text, icon, url, id, options}): MenuItem =>
	({label: {text}, icon, url, id, options});

@Component({
	selector: 'app-menu',
	templateUrl: './menu.component.html',
	styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
	@Input() menuItems: MenuItem[];

	constructor() { }

}
