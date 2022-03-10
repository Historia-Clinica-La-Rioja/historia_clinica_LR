import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './routes/login/login.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
// components
import { AuthComponent } from './auth.component';
import { FormInputComponent } from './components/form-input/form-input.component';
import { AccessDataResetComponent } from './routes/access-data-reset/access-data-reset.component';
import { ClipboardModule } from "@angular/cdk/clipboard";
import { ExternalOAuthLoginComponent } from './routes/external-oauth-login/external-oauth-login.component';
import { OauthLoginComponent } from "./routes/chaco-login/oauth-login.component";
import { HospitalLoginComponent } from './routes/hospital-login/hospital-login.component';

@NgModule({
	declarations: [
		// routing
		AccessDataResetComponent,
		LoginComponent,
		OauthLoginComponent,
		PasswordResetComponent,
		// components
		AuthComponent,
		FormInputComponent,
		OauthLoginComponent,
		AccessDataResetComponent,
		ExternalOAuthLoginComponent,
  		HospitalLoginComponent,
	],
	exports: [
		ExternalOAuthLoginComponent
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		ClipboardModule,
		// routing
		AuthRoutingModule,
		// deps
		PresentationModule,
	]
})
export class AuthModule { }
