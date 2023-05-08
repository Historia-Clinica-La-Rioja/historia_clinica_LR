import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { PresentationModule } from '@presentation/presentation.module';
import { WorklistByTechnicalComponent } from './routes/worklist-by-technical/worklist-by-technical.component';
import { HomeComponent } from './routes/home/home.component';
import { FinishStudyComponent } from './dialogs/finish-study/finish-study.component';
import { StudyStatusPopupComponent } from './dialogs/study-status-popup/study-status-popup.component';

@NgModule({
	declarations: [
		WorklistByTechnicalComponent,
		HomeComponent,
		FinishStudyComponent,
		StudyStatusPopupComponent
	],
	imports: [
		CommonModule,
		PresentationModule,
		ImageNetworkRoutingModule
	]
})

export class ImageNetworkModule { }
