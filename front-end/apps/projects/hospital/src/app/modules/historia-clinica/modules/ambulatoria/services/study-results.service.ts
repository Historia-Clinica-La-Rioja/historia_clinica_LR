import { Injectable } from '@angular/core';
import { DiagnosticReportInfoDto, SnomedDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class StudyResultsService {
	resultStudy$ = new BehaviorSubject<DiagnosticReportInfoDto[]>([]);
	studies: DiagnosticReportInfoDto[];

	setResultStudy(resultStudy: DiagnosticReportInfoDto[]) {
		this.resultStudy$.next(resultStudy);
	}

	getStudies(idOrder: number): StudyInfo[] {

		let resultStudy =  this.resultStudy$.getValue();

		let studiesFilters: DiagnosticReportInfoDto[] = resultStudy.filter((s: DiagnosticReportInfoDto) => s.serviceRequestId === idOrder);

		return studiesFilters.map((s: DiagnosticReportInfoDto) => {
			return {
				idDiagnostic: s.id,
				snomed: s.snomed
			}
		})
	}

}


export interface StudyInfo {
	idDiagnostic: number;
	snomed: SnomedDto;
}
