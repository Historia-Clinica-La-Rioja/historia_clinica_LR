import { Injectable } from '@angular/core';
import { DiagnosticReportInfoDto, GetDiagnosticReportObservationDto, GetDiagnosticReportObservationGroupDto, SnomedDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';
import { PrescripcionesService } from './prescripciones.service';
import { StudyResults } from '../components/show-closed-forms-template/show-closed-forms-template.component';
import { ResultPractice } from '../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';

@Injectable({
	providedIn: 'root'
})
export class StudyResultsService {
	resultStudy$ = new BehaviorSubject<DiagnosticReportInfoDto[]>([]);
	studies: DiagnosticReportInfoDto[];
	constructor(
		private readonly prescripcionesService: PrescripcionesService,
	) { }

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

	getTemplatesStudies(idOrder: number, patientId: number): ResultPractice[] {

		let resultsPractices = [];



		this.getStudies(idOrder).forEach((studie: StudyInfo) => {

			this.prescripcionesService.showStudyResultsWithFormTempalte(patientId, studie.idDiagnostic).subscribe((diagnostic: GetDiagnosticReportObservationGroupDto) => {

				const studyResults: StudyResults[] = [];
				diagnostic.observations.forEach((elem: GetDiagnosticReportObservationDto) => {
					if (elem?.value != "") {
						const result: StudyResults = {
							procedureParameterId: elem.procedureParameterId,
							description: elem.representation.description,
							value: elem.representation.value || elem?.value
						}
						studyResults.push(result);
					}
				});

				const result: ResultPractice = {
					practice: studie.snomed.pt,
					templateResult: studyResults
				}
				resultsPractices.push(result);
			}

			);
		});
		return resultsPractices;
	}


}


export interface StudyInfo {
	idDiagnostic: number;
	snomed: SnomedDto;
}
