import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { DatePipe, registerLocaleData } from '@angular/common';
import localeEsAr from '@angular/common/locales/es-AR';
import localeEsArExtras from '@angular/common/locales/extra/es-AR';
// providers
import { httpInterceptorProviders } from './http-interceptors';
import { pwaInstallProviders } from '@core/services/pwa-install.service';
// routing
import { AppRoutingModule } from './app-routing.module';
// components
import { AppComponent, DEFAULT_LANG } from './app.component';
// deps
import { ApiRestModule } from '@api-rest/api-rest.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

// services
import { PublicService } from '@api-rest/services/public.service';
import { FlavoredMultiTranslateHttpLoader } from '@core/utils/flavored-multi-translate-http-loader';

import { environment } from '@environments/environment';

registerLocaleData(localeEsAr, localeEsArExtras);

@NgModule({
	declarations: [
		// components
		AppComponent,
	],
	imports: [
		BrowserAnimationsModule,
		BrowserModule,
		FormsModule,
		HttpClientModule,
		RouterModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (createTranslateLoader),
				deps: [HttpClient, PublicService]
			}
		}),
		// deps
		ApiRestModule,
		CoreModule,
		PresentationModule,
		// routing https://angular.io/guide/router#module-import-order
		AppRoutingModule,
		ServiceWorkerModule.register('ngsw-worker.js', {
			enabled: environment.production,
		}),
	],
	providers: [
		httpInterceptorProviders,
		pwaInstallProviders,
		DatePipe,
		{ provide: LOCALE_ID, useValue: DEFAULT_LANG }, // Esto lo usa el calendario
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function createTranslateLoader(http: HttpClient, publicService: PublicService): TranslateLoader {
	return new FlavoredMultiTranslateHttpLoader(
		http,
		publicService.getInfo(),
		[
			{ prefix: './assets/i18n/', suffix: '.json' },
			{ prefix: './assets/i18n/auth/', suffix: '.json' },
			{ prefix: './assets/i18n/institucion/', suffix: '.json' },
			{ prefix: './assets/i18n/internacion/', suffix: '.json' },
			{ prefix: './assets/i18n/pacientes/', suffix: '.json' },
			{ prefix: './assets/i18n/ambulatoria/', suffix: '.json' },
			{ prefix: './assets/i18n/historia-clinica/', suffix: '.json' },
			{ prefix: './assets/i18n/turnos/', suffix: '.json' },
			{ prefix: './assets/i18n/camas/', suffix: '.json' },
			{ prefix: './assets/i18n/guardia/', suffix: '.json' },
			{ prefix: './assets/i18n/portal-paciente/', suffix: '.json' },
			{ prefix: './assets/i18n/reportes/', suffix: '.json' },
			{ prefix: './assets/i18n/configuracion/', suffix: '.json' }
		]
	);
}
