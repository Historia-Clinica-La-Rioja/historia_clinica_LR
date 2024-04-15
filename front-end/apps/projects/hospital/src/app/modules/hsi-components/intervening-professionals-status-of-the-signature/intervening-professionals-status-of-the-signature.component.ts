import { Component, Input } from '@angular/core';
import { DocumentElectronicSignatureProfessionalStatusDto } from '@api-rest/api-model';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';
import { SIGNATURE_STATUS_OPTION } from '../../documents-signature/modules/joint-signature/constants/joint-signature.constants';

@Component({
	selector: 'app-intervening-professionals-status-of-the-signature',
	templateUrl: './intervening-professionals-status-of-the-signature.component.html',
	styleUrls: ['./intervening-professionals-status-of-the-signature.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class InterveningProfessionalsStatusOfTheSignatureComponent {
	@Input() set setInterveningProfessionals(interveningProfessionals: DocumentElectronicSignatureProfessionalStatusDto[]) {
		if (interveningProfessionals) {
			this.interveningProfessionals = interveningProfessionals.map(professional => this.getColoredIconText(professional));
		}
	};
	interveningProfessionals: ProfessionalSignatureData[];

	constructor() { }

	private getColoredIconText(professional: DocumentElectronicSignatureProfessionalStatusDto): ProfessionalSignatureData {
		return {
			professionalCompleteName: professional.professionalCompleteName,
			signature: SIGNATURE_STATUS_OPTION[professional.status]
		}
	}
}

interface ProfessionalSignatureData {
	professionalCompleteName: string,
	signature: ColoredIconText
}
