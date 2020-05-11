import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { LoginComponent } from './routes/login/login.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { FormInputComponent } from './components/form-input/form-input.component';
import { HomeComponent } from './routes/home/home.component';

@NgModule({
	declarations: [
		AuthComponent,
		LoginComponent,
		PasswordResetComponent,
		ProfileComponent,
		FormInputComponent,
		HomeComponent,
	],
	imports: [
		CoreModule,
		PresentationModule,
		FormsModule,
		ReactiveFormsModule,
		AuthRoutingModule,
	]
})
export class AuthModule { }
