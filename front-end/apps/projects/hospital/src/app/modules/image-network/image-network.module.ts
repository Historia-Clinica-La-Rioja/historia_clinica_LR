import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { PresentationModule } from '@presentation/presentation.module';
import { HistoriaClinicaModule } from "../historia-clinica/historia-clinica.module";
//routes
import { ImageNetworkRoutingModule } from './image-network-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { StudyDetailsComponent } from './routes/study-details/study-details.component';
import { WorklistByTechnicalComponent } from './routes/worklist-by-technical/worklist-by-technical.component';
//dialogs
import { AddConclusionFormComponent } from './dialogs/add-conclusion-form/add-conclusion-form.component';
import { DeleteTemplateComponent } from './dialogs/delete-template/delete-template.component';
import { DeriveReportComponent } from './dialogs/derive-report/derive-report.component';
import { DownloadTranscribedOrderComponent } from './dialogs/download-transcribed-order/download-transcribed-order.component';
import { FinishStudyComponent } from './dialogs/finish-study/finish-study.component';
import { ImportTemplateComponent } from './dialogs/import-template/import-template.component';
import { IndexImageManuallyComponent } from './dialogs/index-image-manually/index-image-manually.component';
import { IndexingImageStatusComponent } from './dialogs/indexing-image-status/indexing-image-status.component';
import { SaveTemplateComponent } from './dialogs/save-template/save-template.component';
import { StudyStatusPopupComponent } from './dialogs/study-status-popup/study-status-popup.component';
//components
import { DownloadStudyComponent } from './components/download-study/download-study.component';
import { ImageOrderColoredIconTextCasesComponent } from './components/image-order-identifier-cases/image-order-coloredIconText-cases.component';
import { OrderImageDetailComponent } from './components/order-image-detail/order-image-detail.component';
import { ReportStudyComponent } from './components/report-study/report-study.component';
import { TooltipOrderComponent } from './components/tooltip-order/tooltip-order.component';
import { ViewInformerObservationsComponent } from './components/view-informer-observations/view-informer-observations.component';
import { ViewReportComponent } from './components/view-report/view-report.component';
import { ViewStudyComponent } from './components/view-study/view-study.component';
import { WorklistByInformerComponent } from './components/worklist-by-informer/worklist-by-informer.component';
import { WorklistComponent } from './components/worklist/worklist.component';
import { WorklistFiltersComponent } from './components/worklist-filters/worklist-filters.component';
//standalone
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { ConceptTypeaheadSearchComponent } from '../hsi-components/concept-typeahead-search/concept-typeahead-search.component';
//pipes
import { ProfessionalFullNamePipe } from './pipes/professional-full-name.pipe';
import { TechnicalWorklistHomeComponent } from './routes/technical-worklist-home/technical-worklist-home.component';
import { QueueImageTechnicalComponent } from './components/queue-image-technical/queue-image-technical.component';
import { ImageTableTechnicalComponent } from './components/image-table-technical/image-table-technical.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ImageQueueFiltersComponent } from './components/image-queue-filters/image-queue-filters.component';
import { ImageQueuePaginatorComponent } from './components/image-queue-paginator/image-queue-paginator.component';
import { QueueImageListComponent } from './components/queue-image-list/queue-image-list.component';

@NgModule({
    declarations: [
		AddConclusionFormComponent,
		DeleteTemplateComponent,
		DeriveReportComponent,
		DownloadStudyComponent,
		DownloadTranscribedOrderComponent,
		FinishStudyComponent,
		HomeComponent,
		ImageOrderColoredIconTextCasesComponent,
		ImageQueueFiltersComponent,
		ImageQueuePaginatorComponent,
		ImageTableTechnicalComponent,
		ImportTemplateComponent,
		IndexImageManuallyComponent,
		IndexingImageStatusComponent,
		OrderImageDetailComponent,
		ProfessionalFullNamePipe,
		QueueImageListComponent,
		QueueImageTechnicalComponent,
		ReportStudyComponent,
		SaveTemplateComponent,
		StudyDetailsComponent,
		StudyStatusPopupComponent,
		TechnicalWorklistHomeComponent,
		TooltipOrderComponent,
		ViewInformerObservationsComponent,
		ViewReportComponent,
		ViewStudyComponent,
		WorklistByInformerComponent,
		WorklistByTechnicalComponent,
		WorklistComponent,
		WorklistFiltersComponent,
    ],
    imports: [
        CommonModule,
        PresentationModule,
        ImageNetworkRoutingModule,
        HistoriaClinicaModule,
		//standalone
        IdentifierCasesComponent,
		ConceptTypeaheadSearchComponent,
        PatientSummaryComponent
    ],
    exports: [
        ViewStudyComponent,
        ViewReportComponent,
        TooltipOrderComponent,
        ProfessionalFullNamePipe
    ]
})

export class ImageNetworkModule { }
