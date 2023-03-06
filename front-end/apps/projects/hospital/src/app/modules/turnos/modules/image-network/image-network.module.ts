import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
import { PresentationModule } from '@presentation/presentation.module';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { SearchAppointmentsByEquipmentComponent } from './components/search-appointments-by-equipment/search-appointments-by-equipment.component';
import { EquipmentDiaryComponent } from './components/equipment-diary/equipment-diary.component';
import { ImageNetworkAppointmentComponent } from './components/image-network-appointment/image-network-appointment.component';
import { EquipmentAppointmentsFacadeService } from './services/equipment-appointments-facade.service';

@NgModule({
	declarations: [
		EquipmentDiarySetupComponent,
		SearchAppointmentsByEquipmentComponent,
		EquipmentDiaryComponent,
  		ImageNetworkAppointmentComponent
	],
	imports: [
		CommonModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		PresentationModule,
		ImageNetworkRoutingModule
	],
	exports: [
		SearchAppointmentsByEquipmentComponent,
		EquipmentDiaryComponent
	],
	providers: [
		EquipmentAppointmentsFacadeService
	]
})

export class ImageNetworkModule { }
