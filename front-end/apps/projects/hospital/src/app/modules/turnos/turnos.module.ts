import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TurnosRoutingModule } from './turnos-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { NewAttentionComponent } from './dialogs/new-attention/new-attention.component';
import { PresentationModule } from '@presentation/presentation.module';
import { CalendarDateFormatter, CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { AppMaterialModule } from '../material/app.material.module';
import { CustomDateFormatter } from './services/custom-date-formatter.service';
import { NewAppointmentComponent } from './dialogs/new-appointment/new-appointment.component';
import { AppointmentComponent } from './dialogs/appointment/appointment.component';
import { CancelAppointmentComponent } from './dialogs/cancel-appointment/cancel-appointment.component';
import { MedicalCoverageComponent } from '../core/dialogs/medical-coverage/medical-coverage.component';
import { SelectAgendaComponent } from './components/select-agenda/select-agenda.component';
import { AgendaComponent } from './routes/agenda/agenda.component';


@NgModule({
	declarations: [
		HomeComponent,
	 	AgendaSetupComponent,
	 	NewAttentionComponent,
	 	SelectAgendaComponent,
	 	AgendaComponent,
	 	NewAppointmentComponent,
	 	AppointmentComponent,
	 	CancelAppointmentComponent,
	 	MedicalCoverageComponent
	],
	imports: [
		CommonModule,
		CoreModule,
		AppMaterialModule,
		PresentationModule,
		TurnosRoutingModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
	],
	providers: [
		{
			provide: CalendarDateFormatter,
			useClass: CustomDateFormatter,
		}
	]
})
export class TurnosModule {
}
