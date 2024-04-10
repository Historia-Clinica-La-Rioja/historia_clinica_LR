import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { PresentationModule } from '@presentation/presentation.module';
import { InterveningProfessionalsStatusOfTheSignatureComponent } from '../intervening-professionals-status-of-the-signature/intervening-professionals-status-of-the-signature.component';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';

@Component({
	selector: 'app-header-attention-detail',
	templateUrl: './header-attention-detail.component.html',
	styleUrls: ['./header-attention-detail.component.scss'],
	standalone: true,
	imports: [PresentationModule, InterveningProfessionalsStatusOfTheSignatureComponent]
})
export class HeaderAttentionDetailComponent {


	@Input() set setDocumentId(documentId: number) {
		if (documentId)
			this.getProfessionals(documentId);
	};

	@Input() title: string;
	@Input() details: Detail[];
	professionalsList = [];

	constructor(private documentServices: JointSignatureService) {	}

	private getProfessionals(documentId: number): void {
		this.documentServices.getDocumentElectronicSignatureProfessionalStatusController(documentId).subscribe(professionals => {
			this.professionalsList = professionals;
		})
	}

}
