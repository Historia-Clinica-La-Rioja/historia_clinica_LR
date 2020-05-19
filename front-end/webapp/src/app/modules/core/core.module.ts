import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';

import { ContentComponent } from './components/content/content.component';
import { HttpClientModule } from '@angular/common/http';
import { TranslateModule } from "@ngx-translate/core";
import { RECAPTCHA_SETTINGS, RecaptchaFormsModule, RecaptchaModule, RecaptchaSettings } from "ng-recaptcha";
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { HasRoleDirective } from './directives/has-role.directive';

const globalSettings: RecaptchaSettings = { siteKey: '' }; // TODO completar cuando se implemente para esta aplicacion

@NgModule({
	declarations: [
		ContentComponent,
		SidenavComponent,
		ConfirmDialogComponent,
		HasRoleDirective,
	],
	imports: [
		AppMaterialModule,
		CommonModule,
		HttpClientModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		TranslateModule,
	],
	exports: [
		AppMaterialModule,

		CommonModule,
		ConfirmDialogComponent,
		ContentComponent,
		HttpClientModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		SidenavComponent,
		TranslateModule,
		HasRoleDirective,
	],
	providers: [{
		provide: RECAPTCHA_SETTINGS,
		useValue: globalSettings,
	}]
})
export class CoreModule {
}
