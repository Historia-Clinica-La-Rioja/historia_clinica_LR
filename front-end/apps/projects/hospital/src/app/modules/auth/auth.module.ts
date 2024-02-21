import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CodeInputModule } from "angular-code-input";
import { ClipboardModule } from "@angular/cdk/clipboard";
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './routes/login/login.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
import { PasswordRecoverComponent } from './routes/password-recover/password-recover.component';
import { HospitalLoginComponent } from './routes/hospital-login/hospital-login.component';
import { AccessDataResetComponent } from './routes/access-data-reset/access-data-reset.component';
import { ExternalOAuthLoginComponent } from './routes/external-oauth-login/external-oauth-login.component';
import { OauthLoginComponent } from "./routes/chaco-login/oauth-login.component";
// components
import { AuthComponent } from './auth.component';
import { FormInputComponent } from './components/form-input/form-input.component';
import { UpdatePasswordComponent } from "./components/update-password/update-password.component";
import { UpdatePasswordSuccessComponent } from './components/update-password-success/update-password-success.component';
//dialogs
import { LoginPinCodeComponent } from './dialogs/login-pin-code/login-pin-code.component';

@NgModule({
	declarations: [
		// routing
		AccessDataResetComponent,
		LoginComponent,
		OauthLoginComponent,
		PasswordResetComponent,
		PasswordRecoverComponent,
		// components
		AuthComponent,
		FormInputComponent,
		OauthLoginComponent,
		AccessDataResetComponent,
		ExternalOAuthLoginComponent,
  		HospitalLoginComponent,
		UpdatePasswordComponent,
  		UpdatePasswordSuccessComponent,
		// dialogs
    	LoginPinCodeComponent,
	],
	exports: [
		ExternalOAuthLoginComponent,
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		ClipboardModule,
		// routing
		AuthRoutingModule,
		// deps
		PresentationModule,
		CodeInputModule,
	]
})
export class AuthModule { }
