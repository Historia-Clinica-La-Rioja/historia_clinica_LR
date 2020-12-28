import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { PublicInfoDto } from '@api-rest/api-model';
import { PublicService } from '@api-rest/services/public.service';
import { MatIconRegistry } from '@angular/material/icon';
import { PwaInstallService } from '@core/services/pwa-install.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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
		pwaInstallProviders: PwaInstallService,
		snackBarService: SnackBarService,
		private matIconRegistry: MatIconRegistry,
		private domSanitizer: DomSanitizer,
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
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/person_check_outlined.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'person_cancel_outlined',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/person_cancel_outlined.svg')
		);
		//
		pwaInstallProviders.install$.pipe(
			switchMap(
				({event}) =>
					snackBarService.showAction('Instalar aplicación', {text:'Ok', payload: event})
			),
		).subscribe(event => {
			event.prompt();
			// Wait for the user to respond to the prompt
			event.userChoice.then(
				(choiceResult) => console.log(`User ${choiceResult?.outcome}`)
			);
		});
	}

}
