import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';
import { FooterComponent } from './components/footer/footer.component';
import { BarComponent } from './components/bar/bar.component';
import { ContentComponent } from './components/content/content.component';
import { HttpClientModule } from '@angular/common/http';
import { PresentationModule } from '../presentation/presentation.module';
import { TranslateModule } from "@ngx-translate/core";
import { RECAPTCHA_SETTINGS, RecaptchaFormsModule, RecaptchaModule, RecaptchaSettings } from "ng-recaptcha";
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { RouterModule } from '@angular/router';

const globalSettings: RecaptchaSettings = { siteKey: '' }; // TODO completar cuando se implemente para esta aplicacion

@NgModule({
	declarations: [
		BarComponent,
		ContentComponent,
		FooterComponent,
		SidenavComponent,
	],
	imports: [
		AppMaterialModule,
		CommonModule,
		HttpClientModule,
		PresentationModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		TranslateModule,
		RouterModule,
	],
	exports: [
		BarComponent,
		CommonModule,
		ContentComponent,
		FooterComponent,
		HttpClientModule,
		TranslateModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		SidenavComponent,
		RouterModule
	],
	providers: [{
		provide: RECAPTCHA_SETTINGS,
		useValue: globalSettings,
	}]
})
export class CoreModule {
}
