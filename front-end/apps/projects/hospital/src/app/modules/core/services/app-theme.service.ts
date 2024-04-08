import { Injectable } from '@angular/core';
import { Theme } from '@core/components/exchangeable-theme/exchangeable-theme.component';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AppThemeService {

	private readonly themeChangedEmitter = new Subject<Theme>();
	theme$ = this.themeChangedEmitter.asObservable();

	constructor() { }

	themeChanged(theme: Theme) {
		this.themeChangedEmitter.next(theme);
	}
}
