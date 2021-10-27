import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppMaterialModule } from '@material/app.material.module';
import { FlexModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';
import { RecaptchaFormsModule, RecaptchaModule } from 'ng-recaptcha';

import { ContentComponent } from './components/content/content.component';
import { HasRoleDirective } from './directives/has-role.directive';
import { FeatureFlagDirective } from './directives/feature-flag.directive';

import { TypeaheadComponent } from './components/typeahead/typeahead.component';


@NgModule({
	declarations: [
		ContentComponent,
		HasRoleDirective,
		FeatureFlagDirective,
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
		ContentComponent,
		FeatureFlagDirective,
		HasRoleDirective,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		TranslateModule,
		TypeaheadComponent,
	],
	providers: []
})
export class CoreModule {
}
