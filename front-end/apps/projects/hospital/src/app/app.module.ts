import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { DatePipe } from '@angular/common';

import { registerLocaleData } from '@angular/common';
import localeEsAr from '@angular/common/locales/es-AR';

import { httpInterceptorProviders } from './http-interceptors';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
// Módulos nuestros que se cargan al inicio
import { CoreModule } from '@core/core.module';
import { pwaInstallProviders } from '@core/services/pwa-install.service';
import { FlavoredMultiTranslateHttpLoader } from '@core/utils/flavored-multi-translate-http-loader';
import { ApiRestModule } from '@api-rest/api-rest.module';
import { PublicService } from '@api-rest/services/public.service';
import { PresentationModule } from '@presentation/presentation.module';
import { AppMaterialModule } from './modules/material/app.material.module';
import { AuthModule } from './modules/auth/auth.module';
import { environment } from '@environments/environment';
import { ExtensionsModule } from '@extensions/extensions.module';
import { ExchangeableThemeComponent } from './components/exchangeable-theme/exchangeable-theme.component';

registerLocaleData(localeEsAr, 'es-AR');

@NgModule({
	declarations: [
		AppComponent,
		ExchangeableThemeComponent,
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
		// Módulos nuestros que se cargan al inicio
		ApiRestModule,
		AppMaterialModule,
		AuthModule,
		CoreModule,
		PresentationModule,
		// Module import order
		// https://angular.io/guide/router#module-import-order
		AppRoutingModule,
		ExtensionsModule,
		ServiceWorkerModule.register('ngsw-worker.js', {
			enabled: environment.production,
		}),
	],
	providers: [
		httpInterceptorProviders,
		pwaInstallProviders,
		DatePipe,
		{ provide: LOCALE_ID, useValue: 'es-AR' },
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
