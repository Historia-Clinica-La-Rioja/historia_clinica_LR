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
import { CacheSynonymComponent } from './routes/snomed/cache-synonym/cache-synonym.component';
import { CacheTerminologyComponent } from './routes/snomed/cache-terminology/cache-terminology.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ManageKeysComponent } from './routes/manage-keys/manage-keys.component';
import { ProfileComponent } from './routes/profile/profile.component';
// components
import { HomeComponent } from './home.component';
import { FeatureSettingsComponent } from './components/feature-settings/feature-settings.component';
import { LogoSettingsComponent } from './components/logo-settings/logo-settings.component';
import { SnomedCacheComponent } from './components/snomed-cache/snomed-cache.component';
import { SnomedCacheFormComponent } from './components/snomed-cache-form/snomed-cache-form.component';
import { SnomedTerminologyCardComponent } from './components/snomed-terminology-card/snomed-terminology-card.component';
import { UsageCardComponent } from './components/usage-card/usage-card.component';
import { UserKeysComponent } from './components/user-keys/user-keys.component';
import { UserKeysFormComponent } from './components/user-keys-form/user-keys-form.component';

// dialogs
import { ActivateTwoFactorAuthenticationComponent } from "./dialogs/activate-two-factor-authentication/activate-two-factor-authentication.component";
import { GenerateApiKeyFormComponent } from './dialogs/generate-api-key-form/generate-api-key-form.component';


@NgModule({
	declarations: [
		// routing
		CacheSynonymComponent,
		CacheTerminologyComponent,
		InstitucionesComponent,
		ManageKeysComponent,
		ProfileComponent,
		// components
		HomeComponent,
		FeatureSettingsComponent,
		LogoSettingsComponent,
		SnomedCacheComponent,
		SnomedCacheFormComponent,
		UsageCardComponent,
		UserKeysComponent,
		UserKeysFormComponent,
		// dialogs
		ActivateTwoFactorAuthenticationComponent,
		GenerateApiKeyFormComponent,
		SnomedTerminologyCardComponent,
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
