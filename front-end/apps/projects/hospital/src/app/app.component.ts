import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { PublicService } from '@api-rest/services/public.service';
import { PwaInstallService } from '@core/services/pwa-install.service';
import { PwaUpdateService } from '@core/services/pwa-update.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { PublicInfoDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { Theme } from './components/exchangeable-theme/exchangeable-theme.component';

export const DEFAULT_LANG = 'es-AR';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	public publicInfo$: Observable<PublicInfoDto>;
	selectedTheme: Theme;
	isExchangeableTheme$: Observable<boolean>;
	constructor(
		translate: TranslateService,
		titleService: Title,
		publicService: PublicService,
		pwaInstallService: PwaInstallService,
		pwaUpdateService: PwaUpdateService,
		snackBarService: SnackBarService,
		private matIconRegistry: MatIconRegistry,
		private domSanitizer: DomSanitizer,
		private readonly featureFlagService: FeatureFlagService
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
		this.matIconRegistry.addSvgIcon(
			'bell_appointment_call',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/bell_appointment_call.svg')
		);
		//
		pwaInstallService.install$.pipe(
			switchMap(
				pwaInstallAction =>
					snackBarService.showAction('Instalar aplicación', { text: 'Ok', payload: pwaInstallAction })
			),
		).subscribe(pwaInstallAction => pwaInstallAction.run());

		pwaUpdateService.update$.pipe(
			switchMap(
				pwaUpdateAction =>
					snackBarService.showAction('Nueva versión', { text: 'Actualizar', payload: pwaUpdateAction })
			),
		).subscribe(pwaUpdateAction => pwaUpdateAction.run());

		pwaUpdateService.checkForUpdate();

		this.isExchangeableTheme$ = this.featureFlagService.isActive(AppFeature.HABILITAR_INTERCAMBIO_TEMAS);
	}

}
