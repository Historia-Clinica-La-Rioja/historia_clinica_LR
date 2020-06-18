import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthComponent } from './auth.component';
import { PasswordResetComponent } from './routes/password-reset/password-reset.component';
import { LoginComponent } from './routes/login/login.component';
import { OauthLoginComponent } from './routes/chaco-login/oauth-login.component';
import { OauthLoginGuardService } from './routes/chaco-login/oauth-login-guard.service';


const routes: Routes = [
	{
		path: 'auth',
		component: AuthComponent,
		children: [
			{
				path: '',
				redirectTo: 'login',
				pathMatch: 'full',
			},
			{ path: 'password-reset/:token', component: PasswordResetComponent },
			{ path: 'login', component: LoginComponent, canActivate: [OauthLoginGuardService] },
		]
	},
	{
		path: 'oauth',
		component: AuthComponent,
		children: [
			{
				path: 'chaco',
				redirectTo: 'login',
				pathMatch: 'full',
			},
			{ path: 'login', component: OauthLoginComponent }
		]
	},
];

@NgModule({
	imports: [
		RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	providers: [OauthLoginGuardService]
})
export class AuthRoutingModule { }
