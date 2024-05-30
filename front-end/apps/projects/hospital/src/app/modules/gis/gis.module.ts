import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { PresentationModule } from '@presentation/presentation.module';
import { GisRoutingModule } from './gis-routing.module';
//components
import { HomeComponent } from './routes/home/home.component';
import { MapComponent } from './components/map/map.component';
import { LoadingComponent } from './components/loading/loading.component';
import { InstitutionDescriptionComponent } from './components/institution-description/institution-description.component';


@NgModule({
	declarations: [
    	HomeComponent,
     	MapComponent,
      LoadingComponent,
      InstitutionDescriptionComponent
  	],
	imports: [
		CommonModule,
		GisRoutingModule,
		PresentationModule
	]
})
export class GisModule { }
