import { Component, Input } from '@angular/core';

export interface HideShowToggleHeader {
	icon: string;
	text: string;
}

const ADD_ICON = 'add';
const EXPAND_LESS_ICON = 'expand_less';

@Component({
	selector: 'app-hide-show-toggle',
  	templateUrl: './hide-show-toggle.component.html',
  	styleUrls: ['./hide-show-toggle.component.scss']
})
export class HideShowToggleComponent {

	@Input() header: HideShowToggleHeader;
	rightIcon: string = ADD_ICON;
	show: boolean = false;

	toggle = () => {
		this.show = !this.show;
		(this.show) ? this.rightIcon = EXPAND_LESS_ICON : this.rightIcon = ADD_ICON 
	}
}