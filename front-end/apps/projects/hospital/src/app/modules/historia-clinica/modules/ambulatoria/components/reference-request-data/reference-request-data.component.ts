import { Component, Input } from '@angular/core';
import { ReferenceCounterReferenceFileDto, ReferenceDataDto } from '@api-rest/api-model';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from 'projects/hospital/src/app/modules/hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-reference-request-data',
	templateUrl: './reference-request-data.component.html',
	styleUrls: ['./reference-request-data.component.scss']
})
export class ReferenceRequestDataComponent {

	@Input() set setReference(ref: ReferenceCompleteData) {
		this.reference = ref;
		this.clinicalSpecialtiesName = ref.dto.destinationClinicalSpecialties.map(specialty => specialty.name);
	};
	@Input() order: number;
	@Input() status: PrescriptionStatus;
	Color = Color;
	Position = Position;
	identiferCases = IDENTIFIER_CASES;
	_reference: ReferenceCompleteData = null;
	title = '';
	clinicalSpecialtiesName: string[] = [];
	reference: ReferenceCompleteData

	constructor(
		private readonly referenceFileService: ReferenceFileService,
	) { }

	downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

}
export interface ReferenceCompleteData {
	dto: ReferenceDataDto;
	priority: string;
	problems: string;
}

export interface PrescriptionStatus {
	description: string;
	color: Color;
}
