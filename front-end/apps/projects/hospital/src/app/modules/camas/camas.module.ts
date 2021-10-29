import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { InstitucionModule } from '../institucion/institucion.module';

import { CamasRoutingModule } from './camas-routing.module';

import { HomeComponent } from './routes/home/home.component';

@NgModule({
	declarations: [
		HomeComponent,
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		CoreModule,
		PresentationModule,
		InstitucionModule,
		CamasRoutingModule,
	]
})
export class CamasModule {
}
