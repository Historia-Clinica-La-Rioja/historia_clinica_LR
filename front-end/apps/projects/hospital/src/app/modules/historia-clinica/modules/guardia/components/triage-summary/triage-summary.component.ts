import { Component, Input } from '@angular/core';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { Observable, map } from 'rxjs';
import { TriageAsViewFormat, TriageSummaryService } from '../../services/triage-summary.service';
import { Item } from '../emergency-care-evolutions/emergency-care-evolutions.component';
import { DocumentService } from '@api-rest/services/document.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { TranslateService } from '@ngx-translate/core';
import { RiskFactor } from '@presentation/components/factor-de-riesgo-current/factor-de-riesgo.component';
import { TriageCategory } from '../triage-chip/triage-chip.component';

@Component({
    selector: 'app-triage-summary',
    templateUrl: './triage-summary.component.html',
    styleUrls: ['./triage-summary.component.scss']
  })
  export class TriageSummaryComponent  { 

	@Input() set content(triage: Item) {
        this._triage = triage;
        this.fetchSummaryInfo();
    }
	@Input() set episodeId(episodeId: number) {
		this._episodeId = episodeId;
        this.fetchSummaryInfo();
	};
    private _episodeId: number;
    triageSummary: TriageAsViewFormat;
    _triage: Item;
    private documentName = '';
    documentSummary$: Observable<HeaderDescription>;
    isPopUpOpened = false;

	constructor(
        private readonly triageSummaryService: TriageSummaryService,
        private readonly documentService: DocumentService,
        private readonly translateService: TranslateService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
    ) {
        this.documentName = this.translateService.instant('guardia.documents-summary.document-name.TRIAGE');
	}

    private fetchSummaryInfo(){
        if (this._triage && this._episodeId) {
            this.triageSummary = this.triageSummaryService.mapTriageAsViewFormat(this._triage);
            let header$ = this.documentSummaryService.getEmergencyCareDocumentHeader(this._triage.summary.docId, this._episodeId);
            this.documentSummary$ = header$.pipe(map((headerData) => {
                return this.documentSummaryMapperService.mapEmergencyCareToHeaderDescription(headerData, this.documentName, false, false, true);
            }));
        }
    }  
  
    downloadDocument() {
		this.documentService.downloadFile({ filename: this._triage.summary.docFileName, id: this._triage.summary.docId });
	}
}

export interface TriageDetails {
	creationDate: Date;
	category: TriageCategory;
	createdBy: {
		firstName: string,
		lastName: string,
		nameSelfDetermination: string
	};
	doctorsOfficeDescription?: string;
	riskFactors?: {
		bloodOxygenSaturation: {
			value: string,
			effectiveTime: Date
		},
		diastolicBloodPressure?: {
			value: string,
			effectiveTime: Date
		},
		heartRate: {
			value: string,
			effectiveTime: Date
		},
		respiratoryRate: {
			value: string,
			effectiveTime: Date
		},
		systolicBloodPressure?: {
			value: string,
			effectiveTime: Date
		},
		temperature?: {
			value: string,
			effectiveTime: Date
		},
		bloodGlucose?: {
			value: string,
			effectiveTime: Date
		},
		glycosylatedHemoglobin?: {
			value: string,
			effectiveTime: Date
		},
		cardiovascularRisk?: {
			value: string,
			effectiveTime: Date
		},
	};
	appearance?: {
		bodyTemperatureDescription?: string,
		cryingExcessive?: boolean,
		muscleHypertoniaDescription?: string
	};
	breathing?: {
		respiratoryRetractionDescription: string,
		stridor: boolean,
		respiratoryRate: {
			value: string
			effectiveTime: Date,
		},
		bloodOxygenSaturation: {
			value: string
			effectiveTime: Date,
		}
	};
	circulation?: {
		perfusionDescription: string,
		heartRate: {
			value: string
			effectiveTime: Date,
		}
	};
	notes?: string;
	reasons?: string[];
}

export interface RiskFactorFull {
	id: string,
	description: string,
	value: RiskFactor
}

export interface RiskFactorValue {
	value: string,
	effectiveTime: Date
}
