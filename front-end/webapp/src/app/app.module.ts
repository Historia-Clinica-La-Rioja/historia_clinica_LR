import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { DatePipe } from '@angular/common';

import { httpInterceptorProviders } from './http-interceptors';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { environment } from '@environments/environment';
// Módulos nuestros que se cargan al inicio
import { ApiRestModule } from '@api-rest/api-rest.module';
import { CoreModule } from '@core/core.module';
import { AppMaterialModule } from './modules/material/app.material.module';
import { AuthModule } from './modules/auth/auth.module';
import { PresentationModule } from '@presentation/presentation.module';
import { FlavoredMultiTranslateHttpLoader } from '@core/utils/flavored-multi-translate-http-loader';
import { PublicService } from '@api-rest/services/public.service';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

@NgModule({
	declarations: [
		AppComponent,
	],
	imports: [
		BrowserAnimationsModule,
		BrowserModule,
		FormsModule,
		HttpClientModule,
		RouterModule,
		ServiceWorkerModule.register(
			'ngsw-worker.js', { enabled: environment.production }
		),
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
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
	],
	providers: [
		httpInterceptorProviders,
		DatePipe,
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
		]
	);
}
