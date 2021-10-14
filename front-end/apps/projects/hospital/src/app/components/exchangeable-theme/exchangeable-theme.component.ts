import { Component, EventEmitter, Output } from '@angular/core';

@Component({
	selector: 'app-exchangeable-theme',
	templateUrl: './exchangeable-theme.component.html',
	styleUrls: ['./exchangeable-theme.component.scss']
})
export class ExchangeableThemeComponent {

	constructor() { // This is intentional 
	}

	themesOptions: Theme[] = [{
		name: 'Indigo y Rosa',
		class: 'candy-app-theme'
	},
	{
		name: 'Oscuro',
		class: 'dark-theme'
	},
	{
		name: 'Defecto'
	}];
	selectedTheme: Theme;

	@Output() themeChanged = new EventEmitter();
}


export interface Theme {
	class?: string;
	name: string;
}
