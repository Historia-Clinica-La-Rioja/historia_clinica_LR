import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { ExtensionsModule } from '@extensions/extensions.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InstitucionRoutingModule } from './institucion-routing.module';
import { HomeComponent } from './routes/home/home.component';
// components
import { BedDetailComponent } from './components/bed-detail/bed-detail.component';
import { BedFiltersComponent } from './components/bed-filters/bed-filters.component';
import { BedMappingComponent } from './components/bed-mapping/bed-mapping.component';
import { InstitucionComponent } from './institucion.component';
import { SearchPatientComponent } from '../pacientes/component/search-patient/search-patient.component';


@NgModule({
	declarations: [
		// routing
		HomeComponent,
		// components
		BedDetailComponent,
		BedFiltersComponent,
		BedMappingComponent,
		InstitucionComponent,
		SearchPatientComponent
	],
	imports: [
		CommonModule,
		// routing
		InstitucionRoutingModule,
		// deps
		ExtensionsModule,
		LazyMaterialModule,
		PresentationModule,
	],
	exports: [
		BedDetailComponent,
		BedFiltersComponent,
		BedMappingComponent,
		SearchPatientComponent
	]
})
export class InstitucionModule { }
