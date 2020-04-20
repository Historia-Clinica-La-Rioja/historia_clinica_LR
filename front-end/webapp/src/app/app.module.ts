import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppMaterialModule } from './app.material.module';
import { CoreModule } from '@core/core.module';
import { RouterModule } from '@angular/router';
import { ApiRestModule } from '@api-rest/api-rest.module';
import { PresentationModule } from './modules/presentation/presentation.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { httpInterceptorProviders } from './http-interceptors';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { PacientesModule } from './modules/pacientes/pacientes.module';


@NgModule({
	declarations: [
		AppComponent,
	],
	imports: [
		ApiRestModule,
		AppMaterialModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		BrowserModule,
		CoreModule,
		FormsModule,
		HttpClientModule,
		PresentationModule,
		RouterModule,
		ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
		PacientesModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (createTranslateLoader),
				deps: [HttpClient]
			}
		})
	],
	providers: [httpInterceptorProviders],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function createTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
