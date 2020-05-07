import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';

import { LanguageService } from '@core/services/language.service';
import { ContextService } from '@core/services/context.service';
import { MenuItem } from '@core/core-model';

import { SIDEBAR_MENU } from './modules/pacientes/constants/menu';
import { PermissionsService } from './modules/auth/services/permissions.service';
import { map, switchMap } from 'rxjs/operators';
import { InstitutionDto } from '@api-rest/api-model';

const defaultLang = 'es-AR'; // TODO english version 'en-US';

const institutionMenu = (institution: InstitutionDto) => ({
	text: institution.name,
	icon: 'domain',
	url: '/auth',
	permissions: [],
	options: {exact: true},
});


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
		contextService: ContextService,
		permissionsService: PermissionsService,
	) {
		translate.setDefaultLang(defaultLang);
		translate.use(defaultLang);
		this.menuItems$ = permissionsService.filterItems$(SIDEBAR_MENU).pipe(
			// switchMap((menuFiltered: MenuItem[]) =>
			// 	contextService.institution$.pipe(
			// 		map(
			// 			institution => institution ? [
			// 				institutionMenu(institution),
			// 				...menuFiltered
			// 			] : menuFiltered
			// 		)
			// 	)
			// )
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
