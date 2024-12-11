import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EReferenceAdministrativeState, ReferenceCompleteDataDto, ReferenceRequestDto } from '@api-rest/api-model';
import { PrescriptionStatus, ReferenceCompleteData } from '../reference-request-data/reference-request-data.component';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { Observable, map } from 'rxjs';
import { mapToReferenceCompleteData } from '@access-management/utils/mapper.utils';
import { ReportReference } from '../reference-study-closure-information/reference-study-closure-information.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { ButtonService } from '../../services/button.service';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { ResultPractice } from '../../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { StudyInfo } from '../../services/study-results.service';

const COMPLETED = 'Completado'

@Component({
	selector: 'app-reference-complete-study',
	templateUrl: './reference-complete-study.component.html',
	styleUrls: ['./reference-complete-study.component.scss'],
	providers: [ButtonService]
})
export class ReferenceCompleteStudyComponent implements OnInit {

	_reference$: Observable<ReferenceCompleteData>;
	buttonTypeFlat = ButtonType.FLAT;
	boxMessageInfo: BoxMessageInformation;
	destinationState: EReferenceAdministrativeState;
	APPROVED = EReferenceAdministrativeState.APPROVED;
	COMPLETED = COMPLETED;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			reference: ReferenceRequestDto;
			patientId: number,
			referenceId: number,
			diagnosticReportId: number,
			reportReference: ReportReference;
			status: PrescriptionStatus,
			order: number,
			studies: StudyInfo[];
			resultsPractices: ResultPractice[];
		},
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		readonly buttonService: ButtonService,
	) { }

	ngOnInit() {
		const referenceId = this.data.referenceId;
		this._reference$ = this.institutionalReferenceReportService.getReferenceDetail(referenceId).pipe(
			map((referenceComplete: ReferenceCompleteDataDto) => {
				this.destinationState = referenceComplete.administrativeState?.state;
				return mapToReferenceCompleteData(referenceComplete.reference, referenceComplete.regulation.state)
			})
		);

		this.setBoxMessageInfo();
	}

	completeStudy() {
		this.buttonService.submit();
	}

	completePartialStudy() {
		this.buttonService.submitPartialSave();
	}

	private setBoxMessageInfo() {
		this.boxMessageInfo = {
			message: 'ambulatoria.reference-study-close.MENSSAGE_ERROR',
			showButtons: false
		};
	}

}
