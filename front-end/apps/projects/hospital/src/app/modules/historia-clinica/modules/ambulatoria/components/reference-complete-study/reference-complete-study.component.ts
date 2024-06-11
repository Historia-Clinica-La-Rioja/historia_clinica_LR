import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EReferenceRegulationState, ReferenceCompleteDataDto, ReferenceRequestDto } from '@api-rest/api-model';
import { PrescriptionStatus, ReferenceCompleteData } from '../reference-request-data/reference-request-data.component';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { Observable, map } from 'rxjs';
import { mapToReferenceCompleteData } from '@access-management/utils/mapper.utils';
import { ReportReference } from '../reference-study-closure-information/reference-study-closure-information.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { ButtonService } from '../../services/button.service';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { StudyInfo } from '../../services/study-results.service';

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
	APPROVED = EReferenceRegulationState.APPROVED;
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
		},
		private readonly institutionaReferenceReportService: InstitutionalReferenceReportService,
		readonly buttonService: ButtonService,
	) { }

	ngOnInit() {
		const referenceId = this.data.referenceId;
		this._reference$ = this.institutionaReferenceReportService.getReferenceDetail(referenceId).pipe(
			map((referenceComplete: ReferenceCompleteDataDto) =>
				mapToReferenceCompleteData(referenceComplete.reference, referenceComplete.regulation.state)
			)
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
