import { Component, Input } from '@angular/core';
import { ReferenceCounterReferenceFileDto, ReferenceDataDto } from '@api-rest/api-model';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { getColoredIconText, getPriority } from '@turnos/utils/reference.utils';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';

@Component({
	selector: 'app-reference-complete-data',
	templateUrl: './reference-complete-data.component.html',
	styleUrls: ['./reference-complete-data.component.scss']
})
export class ReferenceCompleteDataComponent {

	referenceCompleteData: ReferenceCompleteData;
	Position = Position;
	identiferCases = IDENTIFIER_CASES;

	@Input()
	set reference(value: ReferenceDataDto) {
		if (value)
			this.referenceCompleteData = this.mapToReferenceCompleteData(value);
	};

	constructor(
		private readonly referenceFileService: ReferenceFileService,
	) { }

	downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

	private mapToReferenceCompleteData(referenceData: ReferenceDataDto): ReferenceCompleteData {
		return {
			dto: referenceData,
			priority: getPriority(referenceData.priority.id),
			closureType: getColoredIconText(referenceData.closureType),
			problems: referenceData.problems.join(', ')
		}
	}

}

interface ReferenceCompleteData {
	dto: ReferenceDataDto;
	priority: string;
	closureType: ColoredIconText;
	problems: string;
}
