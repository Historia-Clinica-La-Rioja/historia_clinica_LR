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

@NgModule({
	declarations: [
		// routing
		InstitucionesComponent,
		ProfileComponent,
		SettingsComponent,
		// components
		HomeComponent,
	],
	imports: [
		CommonModule,
		// routing
		HomeRoutingModule,
		// deps
		ExtensionsModule,
		PresentationModule,
	]
})
export class HomeModule { }
