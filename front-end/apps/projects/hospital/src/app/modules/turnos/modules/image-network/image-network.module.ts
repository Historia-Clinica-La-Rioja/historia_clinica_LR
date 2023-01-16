import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';
import { PresentationModule } from '@presentation/presentation.module';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

@NgModule({
	declarations: [
		EquipmentDiarySetupComponent
	],
	imports: [
		CommonModule,
		CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
		PresentationModule,
		ImageNetworkRoutingModule
	]
})

export class ImageNetworkModule { }
