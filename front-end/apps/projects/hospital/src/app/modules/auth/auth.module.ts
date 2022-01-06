import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { AuthRoutingModule } from './auth-routing.module';
import { AccessDataResetComponent } from './routes/access-data-reset/access-data-reset.component';
import { LoginComponent } from './routes/login/login.component';
import { OauthLoginComponent } from './routes/chaco-login/oauth-login.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
// components
import { AuthComponent } from './auth.component';
import { FormInputComponent } from './components/form-input/form-input.component';

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
