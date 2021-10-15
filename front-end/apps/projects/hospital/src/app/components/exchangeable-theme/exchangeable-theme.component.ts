import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, EventEmitter, Output } from '@angular/core';

@Component({
	selector: 'app-exchangeable-theme',
	templateUrl: './exchangeable-theme.component.html',
	styleUrls: ['./exchangeable-theme.component.scss']
})
export class ExchangeableThemeComponent {

	constructor(
		private overlayContainer: OverlayContainer
	) { // This is intentional 
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

	@Output() themeChanged: EventEmitter<Theme> = new EventEmitter();

 	setTheme(theme: Theme) {
		this.selectedTheme = theme;
		this.themeChanged.next(theme);
		this.overlayContainer.getContainerElement().classList.remove(... this.themesOptions.map(a => a.class));
		this.overlayContainer.getContainerElement().classList.add(theme.class);
	} 
}


export interface Theme {
	class?: string;
	name: string;
}
