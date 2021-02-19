import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/modules/material/app.material.module';

import { ContentComponent } from './components/content/content.component';
import { HttpClientModule } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RecaptchaFormsModule, RecaptchaModule } from 'ng-recaptcha';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { HasRoleDirective } from './directives/has-role.directive';
import { FeatureFlagDirective } from './directives/feature-flag.directive';
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';
import { FlexModule } from '@angular/flex-layout';


@NgModule({
	declarations: [
		ContentComponent,
		SidenavComponent,
		ConfirmDialogComponent,
		HasRoleDirective,
		FeatureFlagDirective,
		DatePickerComponent,
		TypeaheadComponent,
	],
	imports: [
		AppMaterialModule,
		CommonModule,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		TranslateModule,
		FlexModule,
	],
	exports: [
		AppMaterialModule,
		CommonModule,
		ConfirmDialogComponent,
		ContentComponent,
		FeatureFlagDirective,
		HasRoleDirective,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		SidenavComponent,
		TranslateModule,
		TypeaheadComponent,
	],
	providers: []
})
export class CoreModule {
}
