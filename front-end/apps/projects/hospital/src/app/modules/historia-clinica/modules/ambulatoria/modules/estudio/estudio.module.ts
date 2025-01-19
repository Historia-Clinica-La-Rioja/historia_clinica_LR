import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudyImagesCardComponent } from './components/study-images-card/study-images-card.component';
import { StudyLaboratoryCardComponent } from './components/study-laboratory-card/study-laboratory-card.component';
import { StudyPathologicAnatomyCardComponent } from './components/study-pathologic-anatomy-card/study-pathologic-anatomy-card.component';
import { StudyHemotherapyCardComponent } from './components/study-hemotherapy-card/study-hemotherapy-card.component';
import { StudySurgicalProcedureCardComponent } from './components/study-surgical-procedure-card/study-surgical-procedure-card.component';
import { StudyOtherProceduresAndPracticesCardComponent } from './components/study-other-procedures-and-practices-card/study-other-procedures-and-practices-card.component';
import { StudyAdviceCardComponent } from './components/study-advice-card/study-advice-card.component';
import { StudyEducationCardComponent } from './components/study-education-card/study-education-card.component';
import { PresentationModule } from '@presentation/presentation.module';
import { StudyListElementComponent } from './components/study-list-element/study-list-element.component';
import { StudyComponent } from './components/study/study.component';
import { ImageNetworkModule } from 'projects/hospital/src/app/modules/image-network/image-network.module';
import { StudyImagesReportsComponent } from './components/study-images-reports/study-images-reports.component';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';

@NgModule({
	declarations: [
		StudyImagesCardComponent,
		StudyLaboratoryCardComponent,
		StudyPathologicAnatomyCardComponent,
		StudyHemotherapyCardComponent,
		StudySurgicalProcedureCardComponent,
		StudyOtherProceduresAndPracticesCardComponent,
		StudyAdviceCardComponent,
		StudyEducationCardComponent,
		StudyListElementComponent,
		StudyComponent,
  		StudyImagesReportsComponent,
	],
	imports: [
		CommonModule,
		PresentationModule,
		ImageNetworkModule
	],
	exports: [
		StudyImagesCardComponent,
		StudyLaboratoryCardComponent,
		StudyPathologicAnatomyCardComponent,
		StudyHemotherapyCardComponent,
		StudySurgicalProcedureCardComponent,
		StudyOtherProceduresAndPracticesCardComponent,
		StudyAdviceCardComponent,
		StudyEducationCardComponent
	],
	providers: [
		AmbulatoriaSummaryFacadeService
	]
})
export class EstudioModule { }
