import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarDateFormatter, CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
// deps
import { AccessManagementModule } from '@access-management/access-management.module';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
import { SharedAppointmentAccessManagementModule } from '@shared-appointment-access-management/shared-appointment-access-management.module';
// routing
import { TurnosRoutingModule } from './turnos-routing.module';
import { AgendaComponent } from './routes/agenda/agenda.component';
import { AgendaSetupComponent } from './routes/agenda-setup/agenda-setup.component';
import { HomeComponent } from './routes/home/home.component';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
// components
import { AppointmentDetailsComponent } from './components/appointment-details/appointment-details.component';
import { AppointmentLabelComponent } from './components/appointment-label/appointment-label.component';
import { AppointmentResultViewComponent } from './components/appointment-result-view/appointment-result-view.component';
import { AppointmentTabsComponent } from './components/appointment-tabs/appointment-tabs.component';
import { AppointmentListComponent } from './dialogs/appointment-list/appointment-list.component';
import { AppointmentListDetailComponent } from './components/appointment-list-detail/appointment-list-detail.component';
import { CalendarEventViewComponent } from './components/calendar-event-view/calendar-event-view.component';
import { CalendarProfessionalViewComponent } from '@turnos/components/calendar-professional-view/calendar-professional-view.component';
import { DateRangeTimeFormComponent } from './components/date-range-time-form/date-range-time-form.component';
import { DiaryInformationComponent } from './components/diary-information/diary-information.component';
import { DiaryLabelComponent } from './components/diary-label/diary-label.component';
import { EquipmentDiaryComponent } from './components/equipment-diary/equipment-diary.component';
import { DiaryLabelsComponent } from './components/diary-labels/diary-labels.component';
import { EquipmentTranscribeOrderPopupComponent } from './dialogs/equipment-transcribe-order-popup/equipment-transcribe-order-popup.component';
import { ExpiredAppointmentMotiveFormComponent } from './components/expired-appointment-motive-form/expired-appointment-motive-form.component';
import { ImageNetworkAppointmentComponent } from './components/image-network-appointment/image-network-appointment.component';
import { MedicalOrderInputComponent } from './components/medical-order-input/medical-order-input.component';
import { SeachAppointmentsByProfessionalComponent } from './components/seach-appointments-by-professional/seach-appointments-by-professional.component';
import { SearchAppointmentsByEquipmentComponent } from './components/search-appointments-by-equipment/search-appointments-by-equipment.component';
import { SearchAppointmentsBySpecialtyComponent } from './components/search-appointments-by-specialty/search-appointments-by-specialty.component';
import { SearchAppointmentsInCareNetworkComponent } from './components/search-appointments-in-care-network/search-appointments-in-care-network.component';
import { SearchCriteriaComponent } from './components/search-criteria/search-criteria.component';
import { SelectAgendaComponent } from './components/select-agenda/select-agenda.component';
import { NoAppointmentAvailableComponent } from './components/no-appointment-available/no-appointment-available.component';
//Standalone Component
import { AvailableAppointmentDataComponent } from './standalone/components/available-appointment-data/available-appointment-data.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
import { ToAvailableAppointmentDataPipe } from './standalone/pipes/to-available-appointment-data.pipe';
// dialogs
import { AppointmentComponent } from './dialogs/appointment/appointment.component';
import { BlockAgendaRangeComponent } from './dialogs/block-agenda-range/block-agenda-range.component';
import { CalendarProfessionalViewDockPopupComponent } from './dialogs/calendar-professional-view-dock-popup/calendar-professional-view-dock-popup.component';
import { CancelAppointmentComponent } from './dialogs/cancel-appointment/cancel-appointment.component';
import { ConfirmBookingComponent } from './dialogs/confirm-booking/confirm-booking.component';
import { NewAppointmentComponent } from './dialogs/new-appointment/new-appointment.component';
import { NewAttentionComponent } from './dialogs/new-attention/new-attention.component';
import { RecurringCancelPopupComponent } from './dialogs/recurring-cancel-popup/recurring-cancel-popup.component';
import { RecurringCustomizePopupComponent } from './dialogs/recurring-customize-popup/recurring-customize-popup.component';
// services
import { CustomDateFormatter } from './services/custom-date-formatter.service';
import { EquipmentAppointmentsFacadeService } from './services/equipment-appointments-facade.service';
// pipes
import { PracticesPipe } from './pipes/practices.pipe';
import { ShowAppointmentOverTurnPipe } from './pipes/show-appointment-over-turn.pipe';
import { ShowTimeSlotDetailsPipe } from './pipes/show-time-slot-details.pipe';
import { TranscribedStudyComponent } from './dialogs/transcribed-study/transcribed-study.component';
import { StudyListTranscribedComponent } from './components/study-list-transcribed/study-list-transcribed.component';

@NgModule({
	declarations: [
		// routing
		AgendaComponent,
		AgendaSetupComponent,
		EquipmentDiarySetupComponent,
		HomeComponent,
		// components
		AppointmentDetailsComponent,
		AppointmentLabelComponent,
		AppointmentResultViewComponent,
		AppointmentTabsComponent,
		AppointmentListComponent,
		AppointmentListDetailComponent,
		CalendarProfessionalViewComponent,
		DateRangeTimeFormComponent,
		DiaryInformationComponent,
		DiaryLabelComponent,
		DiaryLabelsComponent,
		EquipmentDiaryComponent,
		EquipmentTranscribeOrderPopupComponent,
		ExpiredAppointmentMotiveFormComponent,
		ImageNetworkAppointmentComponent,
		MedicalOrderInputComponent,
		SeachAppointmentsByProfessionalComponent,
		SearchAppointmentsByEquipmentComponent,
		SearchAppointmentsBySpecialtyComponent,
		SearchAppointmentsInCareNetworkComponent,
		SearchCriteriaComponent,
		SelectAgendaComponent,
		CalendarEventViewComponent,
		NoAppointmentAvailableComponent,
		// dialogs
		AppointmentComponent,
		BlockAgendaRangeComponent,
		CalendarProfessionalViewDockPopupComponent,
		CancelAppointmentComponent,
		ConfirmBookingComponent,
		NewAppointmentComponent,
		NewAttentionComponent,
		RecurringCustomizePopupComponent,
	  	RecurringCancelPopupComponent,
		RecurringCancelPopupComponent,
  		RecurringCustomizePopupComponent,
		//pipes
		PracticesPipe,
		ShowAppointmentOverTurnPipe,
		ShowTimeSlotDetailsPipe,
  TranscribedStudyComponent,
  StudyListTranscribedComponent,
	],
	imports: [
		CommonModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		// routing
		TurnosRoutingModule,
		// deps
		AccessManagementModule,
		ClipboardModule,
		HistoriaClinicaModule,
		IdentifierCasesComponent,
		LazyMaterialModule,
		SharedAppointmentAccessManagementModule,
		PresentationModule,
		//Standalone Components
		AvailableAppointmentDataComponent,
		PatientSummaryComponent,
		ReferenceStateLabelComponent,
		ToAvailableAppointmentDataPipe
	],
	exports: [
		CalendarProfessionalViewComponent,
		SearchCriteriaComponent,
	],
	providers: [
		{
			provide: CalendarDateFormatter,
			useClass: CustomDateFormatter,
		},
		EquipmentAppointmentsFacadeService,
	]
})
export class TurnosModule {
}
