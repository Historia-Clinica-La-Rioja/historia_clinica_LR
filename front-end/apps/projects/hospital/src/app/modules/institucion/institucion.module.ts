import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { InstitucionRoutingModule } from './institucion-routing.module';
import { InstitucionComponent } from './institucion.component';

import { HomeComponent } from './routes/home/home.component';
import { BedFiltersComponent } from './components/bed-filters/bed-filters.component';
import { BedDetailComponent } from './components/bed-detail/bed-detail.component';
import { BedMappingComponent } from './components/bed-mapping/bed-mapping.component';
import { SearchPatientComponent } from '../pacientes/component/search-patient/search-patient.component';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';


@NgModule({
	declarations: [
		InstitucionComponent,
		HomeComponent,
		BedFiltersComponent,
		BedDetailComponent,
		BedMappingComponent,
		SearchPatientComponent
	],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		InstitucionRoutingModule,
		LazyMaterialModule
	],
	exports: [
		BedFiltersComponent,
		BedDetailComponent,
		BedMappingComponent,
		SearchPatientComponent
	]
})
export class InstitucionModule { }
