import { Injectable } from '@angular/core';
import { SurgicalReportDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class SurgicalReportDocumentSummaryService {

	constructor() { }

	mapToSurgicalReportViewFormat(surgicalReport: SurgicalReportDto): SurgicalReportViewFormat {
		return {

		}
	}
}

export interface SurgicalReportViewFormat {

}

