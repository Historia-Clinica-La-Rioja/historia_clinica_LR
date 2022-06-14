import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
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
//
import { environment } from '@environments/environment';
import { OAuthModule } from "angular-oauth2-oidc";

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
				deps: [HttpClient]
			}
		}),
		// deps
		ApiRestModule,
		CoreModule,
		// routing https://angular.io/guide/router#module-import-order
		AppRoutingModule,
		ServiceWorkerModule.register('ngsw-worker.js', {
			enabled: environment.production,
		}),
		OAuthModule.forRoot(),
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

export function createTranslateLoader(http: HttpClient): TranslateLoader {
	return new MultiTranslateHttpLoader(
		http,
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
			{ prefix: './assets/i18n/configuracion/', suffix: '.json' },
			{ prefix: './assets/i18n/presentation/', suffix: '.json' },
			{ prefix: './assets/i18n/odontologia/', suffix: '.json' },
			{ prefix: './assets/i18n/indicacion/', suffix: '.json' },
			{ prefix: './assets/i18n/home/', suffix: '.json' }
		]
	);
}
