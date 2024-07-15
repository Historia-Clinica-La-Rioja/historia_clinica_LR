import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { SurgicalReportService } from '@api-rest/services/surgical-report.service';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { SurgicalReportDocumentSummaryService, SurgicalReportViewFormat } from '@historia-clinica/services/surgical-report-document-summary.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { TranslateService } from '@ngx-translate/core';
import { Observable, forkJoin, map } from 'rxjs';

@Component({
	selector: 'app-surgical-report-document-summary',
	templateUrl: './surgical-report-document-summary.component.html',
	styleUrls: ['./surgical-report-document-summary.component.scss']
})
export class SurgicalReportDocumentSummaryComponent implements OnInit {

	@Input() isPopUpOpen: boolean;
	@Input() internmentEpisodeId: number;
	@Input() set activeDocument(activeDocument: DocumentSearch) {
		this._activeDocument = activeDocument;
		this.fetchSummaryInfo();
	};
	@Output() resetActiveDocument = new EventEmitter<boolean>();

	_activeDocument: DocumentSearch;

    documentSummary$: Observable<{headerDescription: HeaderDescription, surgicalReport: SurgicalReportViewFormat}>;
    documentName = '';

	constructor(
		private readonly surgicalReportService: SurgicalReportService,
		private readonly documentSummaryService: DocumentsSummaryService,
		private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
		private readonly surgicalReportSummaryService: SurgicalReportDocumentSummaryService,
		private readonly translateService: TranslateService,
	) {
		this.documentName = this.translateService.instant('internaciones.documents-summary.document-name.SURGICAL_REPORT');
	}

	ngOnInit(): void {
	}

	private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let surgicalReport$ = this.surgicalReportService.getSurgicalReport(this.internmentEpisodeId, this._activeDocument.document.id);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, surgicalReport$]).pipe(map(([headerData, surgicalReportData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, this.documentName, this._activeDocument),
                    surgicalReport: this.surgicalReportSummaryService.mapToSurgicalReportViewFormat(surgicalReportData),
            }}));
        }
    }

}
