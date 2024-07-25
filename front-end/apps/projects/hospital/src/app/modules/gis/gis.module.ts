import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { PresentationModule } from '@presentation/presentation.module';
import { GisRoutingModule } from './gis-routing.module';
//components
import { HomeComponent } from './routes/home/home.component';
import { InstitutionDescriptionComponent } from './components/institution-description/institution-description.component';
import { InstitutionDescriptionStepComponent } from './components/institution-description-step/institution-description-step.component';
import { InstitutionDescriptionDetailComponent } from './components/institution-description-detail/institution-description-detail.component';
import { LoadingComponent } from './components/loading/loading.component';
import { MapComponent } from './components/map/map.component';
import { PatientSearchComponent } from './components/patient-search/patient-search.component';
import { ResponsabilityAreaComponent } from './components/responsability-area/responsability-area.component';


@NgModule({
	declarations: [
    	HomeComponent,
		InstitutionDescriptionComponent,
		InstitutionDescriptionStepComponent,
		InstitutionDescriptionDetailComponent,
		LoadingComponent,
		MapComponent,
		PatientSearchComponent,
		ResponsabilityAreaComponent,
  	],
	imports: [
		CommonModule,
		GisRoutingModule,
		PresentationModule
	],
})
export class GisModule { }
