import { Component, Input } from '@angular/core';
import { ReferenceCounterReferenceFileDto, ReferenceDataDto } from '@api-rest/api-model';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';
import { mapToReferenceCompleteData } from '@access-management/utils/mapper.utils';

@Component({
	selector: 'app-reference-complete-data',
	templateUrl: './reference-complete-data.component.html',
	styleUrls: ['./reference-complete-data.component.scss']
})
export class ReferenceCompleteDataComponent {

	referenceCompleteData: ReferenceCompleteData;
	Position = Position;
	identiferCases = IDENTIFIER_CASES;
	clinicalSpecialtiesName: string[] = [];

	@Input()
	set reference(value: ReferenceDataDto) {
		if (value) {
			this.referenceCompleteData = mapToReferenceCompleteData(value);
			this.clinicalSpecialtiesName = this.referenceCompleteData.dto.destinationClinicalSpecialties.map(specialty => specialty.name);
		}
	};

	constructor(
		private readonly referenceFileService: ReferenceFileService,
	) { }

	downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

}

interface ReferenceCompleteData {
	dto: ReferenceDataDto;
	priority: string;
	problems: string;
}
