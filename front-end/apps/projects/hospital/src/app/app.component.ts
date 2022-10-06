import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { PwaInstallService } from '@core/services/pwa-install.service';
import { PwaUpdateService } from '@core/services/pwa-update.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { AppFeature } from '@api-rest/api-model';
import { Theme } from '@core/components/exchangeable-theme/exchangeable-theme.component';

export const DEFAULT_LANG = 'es-AR';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	selectedTheme: Theme;
	isExchangeableTheme$: Observable<boolean>;
	constructor(
		translate: TranslateService,
		titleService: Title,
		pwaInstallService: PwaInstallService,
		pwaUpdateService: PwaUpdateService,
		snackBarService: SnackBarService,
		private matIconRegistry: MatIconRegistry,
		private domSanitizer: DomSanitizer,
		private readonly featureFlagService: FeatureFlagService,
	) {
		translate.setDefaultLang(DEFAULT_LANG);
		translate.use(DEFAULT_LANG);

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
		this.matIconRegistry.addSvgIcon(
			'pharmaco',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/pharmaco.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'parenteral_plans',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/parenteral_plans.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'assignment_return',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/assignment_return.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'images',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/images.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'laboratory',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/laboratory.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'pathologic_anatomy',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/pathologic_anatomy.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'hemotherapy',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/hemotherapy.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'surgical_procedure',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/surgical_procedure.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'other_procedures_and_practices',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/other_procedures_and_practices.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'advice',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/advice.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'education',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/education.svg')
		);
		this.matIconRegistry.addSvgIcon(
			'clean_hands',
			this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/clean_hands.svg')
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
