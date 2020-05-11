import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';

import { LanguageService } from '@core/services/language.service';
import { MenuItem } from '@core/core-model';

import { SIDEBAR_MENU } from './modules/pacientes/constants/menu';

const defaultLang = 'es-AR'; // TODO english version 'en-US';


@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	currentBrowserLanguage = this.translate.getBrowserLang();
	menuItems$: Observable<MenuItem[]>;

	constructor(
		private translate: TranslateService,
		private languageService: LanguageService,
	) {
		translate.setDefaultLang(defaultLang);
		translate.use(defaultLang);
		this.menuItems$ = of(SIDEBAR_MENU);
	}

	ngOnInit() {
		this.languageService.getCurrentLanguage().subscribe(
			data => {
				if (data[0] && data[0] !== defaultLang) {
					this.switchLanguage(data[0]);
				}
			},
			() => this.switchLanguage(defaultLang)
		);


	}

	switchLanguage(lang: string) {
		this.translate.use(lang);
	}
}
