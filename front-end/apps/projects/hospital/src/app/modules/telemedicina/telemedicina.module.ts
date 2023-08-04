import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { TelemedicinaRoutingModule } from './telemedicina-routing.module.';
import { PresentationModule } from '@presentation/presentation.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { RequestsComponent } from './components/requests/requests.component';
import { NewTelemedicineRequestComponent } from './dialogs/new-telemedicine-request/new-telemedicine-request.component';
import { InformationRequestFormComponent } from './components/information-request-form/information-request-form.component';
import { PacientesModule } from '@pacientes/pacientes.module';



@NgModule({
	declarations: [
		HomeComponent,
		RequestsComponent,
		NewTelemedicineRequestComponent,
		InformationRequestFormComponent,
	],
	imports: [
		CommonModule,
		TelemedicinaRoutingModule,
		LazyMaterialModule,
		PresentationModule,
		PacientesModule,
	]
})
export class TelemedicinaModule { }
