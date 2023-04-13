import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { PresentationModule } from '@presentation/presentation.module';
import { WorklistComponent } from './routes/worklist/worklist.component';

@NgModule({
	declarations: [
		WorklistComponent
	],
	imports: [
		CommonModule,
		PresentationModule,
		ImageNetworkRoutingModule
	]
})

export class ImageNetworkModule { }
