import { NgModule } from '@angular/core';
import { AppMaterialModule } from '../../app.material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { LoginComponent } from './routes/login/login.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
import { CoreModule } from '@core/core.module';

@NgModule({
	declarations: [
		AuthComponent,
		LoginComponent,
		PasswordResetComponent,
	],
	imports: [
		CoreModule,
		AppMaterialModule,
		FormsModule,
		ReactiveFormsModule,
		AuthRoutingModule,
	]
})
export class AuthModule { }
