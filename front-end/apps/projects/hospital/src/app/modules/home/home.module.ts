import { ClipboardModule } from '@angular/cdk/clipboard';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QRCodeModule } from 'angularx-qrcode';
import { CodeInputModule } from 'angular-code-input';

// deps
import { ExtensionsModule } from '@extensions/extensions.module';
import { PresentationModule } from '@presentation/presentation.module';
import { LoincFormModule } from '../hsi-components/loinc-form/loinc-form.module';
// routing
import { HomeRoutingModule } from './home-routing.module';
import { CacheSynonymComponent } from './routes/snomed/cache-synonym/cache-synonym.component';
import { CacheTerminologyComponent } from './routes/snomed/cache-terminology/cache-terminology.component';
import { DocumentImagesComponent } from './routes/appearance/document-images/document-images.component';
import { FaviconsComponent } from './routes/appearance/favicons/favicons.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';
import { ManageKeysComponent } from './routes/manage-keys/manage-keys.component';
import { NotificationTemplatesComponent } from './routes/appearance/notification-templates/notification-templates.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SponsorComponent } from './routes/appearance/sponsor/sponsor.component';
import { TemplateRenderComponent } from './routes/template-render/template-render.component';
// components
import { HomeComponent } from './home.component';
import { AssetFormComponent } from './components/asset-form/asset-form.component';
import { FeatureSettingsComponent } from './components/feature-settings/feature-settings.component';
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
		DocumentImagesComponent,
		FaviconsComponent,
		InstitucionesComponent,
		ManageKeysComponent,
		NotificationTemplatesComponent,
		ProfileComponent,
		SponsorComponent,
		TemplateRenderComponent,
		// components
		HomeComponent,
		AssetFormComponent,
		FeatureSettingsComponent,
		SnomedCacheComponent,
		SnomedCacheFormComponent,
		SnomedTerminologyCardComponent,
		UsageCardComponent,
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
		LoincFormModule,
	]
})
export class HomeModule { }
