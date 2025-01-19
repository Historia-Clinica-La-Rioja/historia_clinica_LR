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
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { CoreModule } from '@core/core.module';
import { ButtonAndMotiveListComponent } from './components/button-and-motive-list/button-and-motive-list.component';
import { ButtonAndProblemListComponent } from './components/button-and-problem-list/button-and-problem-list.component';
import { ToggleAvailabilityComponent } from './components/toggle-availability/toggle-availability.component';
import { RequestAttentionComponent } from './components/request-attention/request-attention.component';
import { RequestInfoCardComponent } from './components/request-info-card/request-info-card.component';
import { AvailableLabelComponent } from './components/available-label/available-label.component';
import { InProgressCallComponent } from './components/in-progress-call/in-progress-call.component';
import { VirtualConsultationsFacadeService } from './virtual-consultations-facade.service';
import { EntryCallRendererComponent } from './components/entry-call-renderer/entry-call-renderer.component';
import { TransferRequestComponent } from './dialogs/transfer-request/transfer-request.component';
import { CallDetailsComponent } from './components/call-details/call-details.component';



@NgModule({
	declarations: [
		HomeComponent,
		RequestsComponent,
		NewTelemedicineRequestComponent,
		InformationRequestFormComponent,
		ButtonAndMotiveListComponent,
		ButtonAndProblemListComponent,
		RequestAttentionComponent,
		ToggleAvailabilityComponent,
		RequestInfoCardComponent,
		AvailableLabelComponent,
		InProgressCallComponent,
		EntryCallRendererComponent,
 		TransferRequestComponent,
	],
	imports: [
		CommonModule,
		TelemedicinaRoutingModule,
		LazyMaterialModule,
		PresentationModule,
		PacientesModule,
		HistoriaClinicaModule,
		CoreModule,
		//Standalone
		CallDetailsComponent,
	],
	providers: [VirtualConsultationsFacadeService,]
})
export class TelemedicinaModule { }
