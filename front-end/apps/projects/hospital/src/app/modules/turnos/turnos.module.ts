import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { TurnosRoutingModule } from './turnos-routing.module';

import { CalendarDateFormatter, CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CustomDateFormatter } from './services/custom-date-formatter.service';

import { HomeComponent } from './routes/home/home.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { NewAttentionComponent } from './dialogs/new-attention/new-attention.component';
import { NewAppointmentComponent } from './dialogs/new-appointment/new-appointment.component';
import { AppointmentComponent } from './dialogs/appointment/appointment.component';
import { CancelAppointmentComponent } from './dialogs/cancel-appointment/cancel-appointment.component';
import { MedicalCoverageComponent } from '@presentation/dialogs/medical-coverage/medical-coverage.component';
import { SelectAgendaComponent } from './components/select-agenda/select-agenda.component';
import { AgendaComponent } from './routes/agenda/agenda.component';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';


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
		PresentationModule,
		TurnosRoutingModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		LazyMaterialModule
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
