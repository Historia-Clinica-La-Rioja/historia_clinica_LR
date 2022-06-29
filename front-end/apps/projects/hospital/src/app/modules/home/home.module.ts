import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { ExtensionsModule } from '@extensions/extensions.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { HomeRoutingModule } from './home-routing.module';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SettingsComponent } from './routes/settings/settings.component';
// components
import { HomeComponent } from './home.component';
import { LogoSettingsComponent } from './components/logo-settings/logo-settings.component';
import { FeatureSettingsComponent } from './components/feature-settings/feature-settings.component';
// dialogs
import { ActivateTwoFactorAuthenticationComponent } from "./dialogs/activate-two-factor-authentication/activate-two-factor-authentication.component";
import { QRCodeModule } from "angularx-qrcode";
import { CodeInputModule } from "angular-code-input";


@NgModule({
	declarations: [
		// routing
		InstitucionesComponent,
		ProfileComponent,
		SettingsComponent,
		// components
		HomeComponent,
		LogoSettingsComponent,
		FeatureSettingsComponent,
		// dialogs
		ActivateTwoFactorAuthenticationComponent
	],
	imports: [
		CommonModule,
		// routing
		HomeRoutingModule,
		// deps
		ExtensionsModule,
		PresentationModule,
		QRCodeModule,
		CodeInputModule,
	]
})
export class HomeModule { }
