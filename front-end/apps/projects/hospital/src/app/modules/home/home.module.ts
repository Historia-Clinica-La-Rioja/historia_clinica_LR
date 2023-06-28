import { ClipboardModule } from '@angular/cdk/clipboard';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QRCodeModule } from 'angularx-qrcode';
import { CodeInputModule } from 'angular-code-input';

// deps
import { ExtensionsModule } from '@extensions/extensions.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { HomeRoutingModule } from './home-routing.module';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SettingsComponent } from './routes/settings/settings.component';
import { ManageKeysComponent } from './routes/manage-keys/manage-keys.component';
// components
import { HomeComponent } from './home.component';
import { FeatureSettingsComponent } from './components/feature-settings/feature-settings.component';
import { LogoSettingsComponent } from './components/logo-settings/logo-settings.component';
import { SnomedCacheComponent } from './components/snomed-cache/snomed-cache.component';
import { SnomedCacheFormComponent } from './components/snomed-cache-form/snomed-cache-form.component';
import { UserKeysComponent } from './components/user-keys/user-keys.component';
import { UserKeysFormComponent } from './components/user-keys-form/user-keys-form.component';

// dialogs
import { ActivateTwoFactorAuthenticationComponent } from "./dialogs/activate-two-factor-authentication/activate-two-factor-authentication.component";
import { GenerateApiKeyFormComponent } from './dialogs/generate-api-key-form/generate-api-key-form.component';

@NgModule({
	declarations: [
		// routing
		InstitucionesComponent,
		ProfileComponent,
		SettingsComponent,
		ManageKeysComponent,
		// components
		HomeComponent,
		FeatureSettingsComponent,
		LogoSettingsComponent,
		SnomedCacheComponent,
		SnomedCacheFormComponent,
		UserKeysComponent,
		UserKeysFormComponent,
		// dialogs
		ActivateTwoFactorAuthenticationComponent,
		GenerateApiKeyFormComponent,
	],
	imports: [
		CommonModule,
		ClipboardModule,
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
