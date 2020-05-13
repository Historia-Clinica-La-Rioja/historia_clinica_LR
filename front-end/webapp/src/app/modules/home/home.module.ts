import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { ProfileComponent } from './routes/profile/profile.component';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { InstitucionesComponent } from './routes/instituciones/instituciones.component';

@NgModule({
	declarations: [
		HomeComponent,
		ProfileComponent,
		InstitucionesComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		HomeRoutingModule
	]
})
export class HomeModule { }
