import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { EquipmentDiarySetupComponent } from './routes/equipment-diary-setup/equipment-diary-setup.component';

@NgModule({
	declarations: [
		EquipmentDiarySetupComponent
	],
	imports: [
		CommonModule,
		ImageNetworkRoutingModule
	]
})

export class ImageNetworkModule { }
