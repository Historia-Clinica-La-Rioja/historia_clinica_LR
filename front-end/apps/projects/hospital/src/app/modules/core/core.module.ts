import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';
import { RecaptchaFormsModule, RecaptchaModule } from 'ng-recaptcha';
// deps
import { CoreMaterialModule } from './core.material.module';
import { AppMaterialModule } from '../material/app.material.module';
// components
import { ContentComponent } from './components/content/content.component';
import { ExchangeableThemeComponent } from './components/exchangeable-theme/exchangeable-theme.component';
// directives
import { HasRoleDirective } from './directives/has-role.directive';
import { FeatureFlagDirective } from './directives/feature-flag.directive';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarModule, DateAdapter } from 'angular-calendar';

@NgModule({
	declarations: [
		// components
		ContentComponent,
		ExchangeableThemeComponent,
		// directives
		FeatureFlagDirective,
		HasRoleDirective,
	],
	imports: [
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		CommonModule,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		TranslateModule,
		FlexModule,
		// deps
		CoreMaterialModule,
		AppMaterialModule,
	],
	exports: [
		CommonModule,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		RecaptchaModule,
		RecaptchaFormsModule,
		RouterModule,
		TranslateModule,
		// deps
		CoreMaterialModule,
		AppMaterialModule,
		// components
		ContentComponent,
		ExchangeableThemeComponent,
		// directives
		FeatureFlagDirective,
		HasRoleDirective,
	],
	providers: []
})
export class CoreModule {
}
