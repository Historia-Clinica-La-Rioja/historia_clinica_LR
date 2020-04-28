import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from '@core/services/language.service';
import { MenuItem } from '@core/core-model';
import { SIDEBAR_MENU } from './modules/pacientes/constants/menu';
import { PermissionsService } from './modules/auth/services/permissions.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

const defaultLang = 'es-AR'; // TODO english version 'en-US';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	title = 'sgh';
	currentBrowserLanguage = this.translate.getBrowserLang();
	menuItems$: Observable<MenuItem[]>;

	constructor(
		private translate: TranslateService,
		private languageService: LanguageService,
		private router: Router,
		permissionsService: PermissionsService,
	) {
		translate.setDefaultLang(defaultLang);
		translate.use(defaultLang);
		this.menuItems$ = permissionsService.filterItems$(SIDEBAR_MENU).pipe(
			tap((items: MenuItem[]) => items && items.length > 0 && this.router.navigate([items[0].url])),
		);
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
