import { Component } from '@angular/core';
import { AppThemeService } from '@core/services/app-theme.service';

@Component({
	selector: 'app-exchangeable-theme',
	templateUrl: './exchangeable-theme.component.html',
	styleUrls: ['./exchangeable-theme.component.scss']
})
export class ExchangeableThemeComponent {

	constructor(
		private readonly appThemeService: AppThemeService
	) { // This is intentional
	}

	readonly themesOptions: Theme[] = [{
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

 	setTheme(theme: Theme) {
		this.appThemeService.themeChanged(theme);
	}
}

export interface Theme {
	class?: string;
	name: string;
}
