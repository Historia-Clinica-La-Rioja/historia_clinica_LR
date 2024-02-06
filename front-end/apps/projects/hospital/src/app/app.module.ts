import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { DatePipe, registerLocaleData, TitleCasePipe } from '@angular/common';
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
import { StompService } from './stomp.service';
import { stompServiceFactory } from './stomp-factory';
import { ApiOverlayDelayComponent } from './api-overlay-delay/api-overlay-delay.component';

registerLocaleData(localeEsAr, localeEsArExtras);

@NgModule({
	declarations: [
		// components
		AppComponent,
		ApiOverlayDelayComponent,
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
		TitleCasePipe,
		{ provide: LOCALE_ID, useValue: DEFAULT_LANG }, // Esto lo usa el calendario
		{ provide: StompService, useFactory: stompServiceFactory }
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function createTranslateLoader(http: HttpClient): TranslateLoader {
	const suffix = `.json?ts=${Date.now()}`;
	return new MultiTranslateHttpLoader(
		http,
		[
			{ prefix: './assets/i18n/', suffix },
			{ prefix: './assets/i18n/auth/', suffix },
			{ prefix: './assets/i18n/institucion/', suffix },
			{ prefix: './assets/i18n/internacion/', suffix },
			{ prefix: './assets/i18n/pacientes/', suffix },
			{ prefix: './assets/i18n/ambulatoria/', suffix },
			{ prefix: './assets/i18n/historia-clinica/', suffix },
			{ prefix: './assets/i18n/turnos/', suffix },
			{ prefix: './assets/i18n/camas/', suffix },
			{ prefix: './assets/i18n/guardia/', suffix },
			{ prefix: './assets/i18n/portal-paciente/', suffix },
			{ prefix: './assets/i18n/reportes/', suffix },
			{ prefix: './assets/i18n/configuracion/', suffix },
			{ prefix: './assets/i18n/presentation/', suffix },
			{ prefix: './assets/i18n/odontologia/', suffix },
			{ prefix: './assets/i18n/indicacion/', suffix },
			{ prefix: './assets/i18n/home/', suffix },
			{ prefix: './assets/i18n/image-network/', suffix },
			{ prefix: './assets/i18n/telemedicina/', suffix },
			{ prefix: './assets/i18n/digital-signature/', suffix},
			{ prefix: './assets/i18n/access-management/', suffix},
			{ prefix: './assets/i18n/hsi-components/', suffix},
			{ prefix: './assets/i18n/call-center/', suffix},
		]
	);
}
