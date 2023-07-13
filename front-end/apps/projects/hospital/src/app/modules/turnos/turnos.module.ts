import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarDateFormatter, CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
// deps
import { PresentationModule } from '@presentation/presentation.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
// routing
import { TurnosRoutingModule } from './turnos-routing.module';
import { AgendaComponent } from './routes/agenda/agenda.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { HomeComponent } from './routes/home/home.component';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
// components
import { AppointmentDetailsComponent } from './components/appointment-details/appointment-details.component';
import { AppointmentResultViewComponent } from './components/appointment-result-view/appointment-result-view.component';
import { CalendarProfessionalViewComponent } from '@turnos/components/calendar-professional-view/calendar-professional-view.component';
import { DateRangeTimeFormComponent } from './components/date-range-time-form/date-range-time-form.component';
import { EquipmentDiaryComponent } from './components/equipment-diary/equipment-diary.component';
import { EquipmentTranscribeOrderPopupComponent } from './dialogs/equipment-transcribe-order-popup/equipment-transcribe-order-popup.component';
import { ImageNetworkAppointmentComponent } from './components/image-network-appointment/image-network-appointment.component';
import { ProfessionalSelectComponent } from './components/professional-select/professional-select.component';
import { SeachAppointmentsByProfessionalComponent } from './components/seach-appointments-by-professional/seach-appointments-by-professional.component';
import { SearchAppointmentsByEquipmentComponent } from './components/search-appointments-by-equipment/search-appointments-by-equipment.component';
import { SearchAppointmentsBySpecialtyComponent } from './components/search-appointments-by-specialty/search-appointments-by-specialty.component';
import { SearchAppointmentsInCareNetworkComponent } from './components/search-appointments-in-care-network/search-appointments-in-care-network.component';
import { SelectAgendaComponent } from './components/select-agenda/select-agenda.component';
// dialogs
import { AppointmentComponent } from './dialogs/appointment/appointment.component';
import { BlockAgendaRangeComponent } from './dialogs/block-agenda-range/block-agenda-range.component';
import { CalendarProfessionalViewDockPopupComponent } from './dialogs/calendar-professional-view-dock-popup/calendar-professional-view-dock-popup.component';
import { CancelAppointmentComponent } from './dialogs/cancel-appointment/cancel-appointment.component';
import { ConfirmBookingComponent } from './dialogs/confirm-booking/confirm-booking.component';
import { ConfirmPrintAppointmentComponent } from './dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { NewAppointmentComponent } from './dialogs/new-appointment/new-appointment.component';
import { NewAttentionComponent } from './dialogs/new-attention/new-attention.component';
// services
import { CustomDateFormatter } from './services/custom-date-formatter.service';
import { EquipmentAppointmentsFacadeService } from './services/equipment-appointments-facade.service';


@NgModule({
	declarations: [
		// routing
		AgendaComponent,
		AgendaSetupComponent,
		EquipmentDiarySetupComponent,
		HomeComponent,
		// components
		AppointmentDetailsComponent,
		AppointmentResultViewComponent,
		CalendarProfessionalViewComponent,
		DateRangeTimeFormComponent,
		EquipmentDiaryComponent,
		EquipmentTranscribeOrderPopupComponent,
		ImageNetworkAppointmentComponent,
		ProfessionalSelectComponent,
		SeachAppointmentsByProfessionalComponent,
		SearchAppointmentsByEquipmentComponent,
		SearchAppointmentsBySpecialtyComponent,
		SearchAppointmentsInCareNetworkComponent,
		SelectAgendaComponent,
		// dialogs
		AppointmentComponent,
		BlockAgendaRangeComponent,
		CalendarProfessionalViewDockPopupComponent,
		CancelAppointmentComponent,
		ConfirmBookingComponent,
		ConfirmPrintAppointmentComponent,
		NewAppointmentComponent,
		NewAttentionComponent,
	],
	imports: [
		CommonModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		// routing
		TurnosRoutingModule,
		// deps
		PresentationModule,
		LazyMaterialModule,
		HistoriaClinicaModule
	],
	exports: [
		CalendarProfessionalViewComponent,
	],
	providers: [
		{
			provide: CalendarDateFormatter,
			useClass: CustomDateFormatter,
		},
		EquipmentAppointmentsFacadeService
	]
})
export class TurnosModule {
}
