import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { PresentationModule } from '@presentation/presentation.module';

@NgModule({
	declarations: [
	],
	imports: [
		CommonModule,
		PresentationModule,
		ImageNetworkRoutingModule
	]
})

export class ImageNetworkModule { }
