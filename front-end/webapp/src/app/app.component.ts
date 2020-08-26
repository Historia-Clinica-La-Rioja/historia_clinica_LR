import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';

import { PublicInfoDto } from '@api-rest/api-model';
import { PublicService } from '@api-rest/services/public.service';
import { MatIconRegistry } from '@angular/material/icon';

const DEFAULT_LANG = 'es-AR';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	public publicInfo$: Observable<PublicInfoDto>;

	constructor(
		translate: TranslateService,
		titleService: Title,
		publicService: PublicService,
		private matIconRegistry: MatIconRegistry,
		private domSanitizer: DomSanitizer
	) {
		translate.setDefaultLang(DEFAULT_LANG);
		translate.use(DEFAULT_LANG);

		this.publicInfo$ = publicService.getInfo();

		translate.onLangChange.subscribe(() => {
			// Change page title when user changes language preference
			translate.get('app.TITLE').subscribe((res: string) => {
				titleService.setTitle(res);
			});
		});

		// Registers custom icons to use within the tag mat-icon
		this.matIconRegistry.addSvgIcon(
			'person_check_outlined',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/person_check_outlined.svg'));
		this.matIconRegistry.addSvgIcon(
			'person_cancel_outlined',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/person_cancel_outlined.svg'));


	}

}
