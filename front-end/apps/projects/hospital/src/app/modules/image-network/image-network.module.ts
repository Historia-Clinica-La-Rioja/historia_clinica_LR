import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { PresentationModule } from '@presentation/presentation.module';
import { WorklistByTechnicalComponent } from './routes/worklist-by-technical/worklist-by-technical.component';
import { HomeComponent } from './routes/home/home.component';
import { FinishStudyComponent } from './dialogs/finish-study/finish-study.component';
import { StudyStatusPopupComponent } from './dialogs/study-status-popup/study-status-popup.component';
import { WorklistComponent } from './components/worklist/worklist.component';
import { WorklistByInformerComponent } from './components/worklist-by-informer/worklist-by-informer.component';
import { StudyDetailsComponent } from './routes/study-details/study-details.component';
import { HistoriaClinicaModule } from "../historia-clinica/historia-clinica.module";
import { ReportStudyComponent } from './components/report-study/report-study.component';
import { ViewStudyComponent } from './components/view-study/view-study.component';
import { DeriveReportComponent } from './dialogs/derive-report/derive-report.component';
import { AddConclusionFormComponent } from './dialogs/add-conclusion-form/add-conclusion-form.component';

@NgModule({
    declarations: [
        WorklistByTechnicalComponent,
        HomeComponent,
        FinishStudyComponent,
        StudyStatusPopupComponent,
        WorklistComponent,
        WorklistByInformerComponent,
        StudyDetailsComponent,
        ReportStudyComponent,
        ViewStudyComponent,
        DeriveReportComponent,
        AddConclusionFormComponent
    ],
    imports: [
        CommonModule,
        PresentationModule,
        ImageNetworkRoutingModule,
        HistoriaClinicaModule
    ],
    exports: [
        ViewStudyComponent
    ]
})

export class ImageNetworkModule { }
