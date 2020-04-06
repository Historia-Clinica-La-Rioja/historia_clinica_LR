import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from '@core/services/language.service';

const defaultLang = 'es-AR'; // TODO english version 'en-US';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	title = 'sgh';
	currentBrowserLanguage = this.translate.getBrowserLang();

	constructor(private translate: TranslateService,
				private languageService: LanguageService) {
		translate.setDefaultLang(defaultLang);
		translate.use(defaultLang);
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
