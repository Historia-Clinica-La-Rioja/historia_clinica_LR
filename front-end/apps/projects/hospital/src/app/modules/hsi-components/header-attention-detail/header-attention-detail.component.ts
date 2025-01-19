import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { PresentationModule } from '@presentation/presentation.module';
import { InterveningProfessionalsStatusOfTheSignatureComponent } from '../intervening-professionals-status-of-the-signature/intervening-professionals-status-of-the-signature.component';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { DocumentElectronicSignatureProfessionalStatusDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { Position } from '@presentation/components/identifier/identifier.component';

@Component({
	selector: 'app-header-attention-detail',
	templateUrl: './header-attention-detail.component.html',
	styleUrls: ['./header-attention-detail.component.scss'],
	standalone: true,
	imports: [PresentationModule, InterveningProfessionalsStatusOfTheSignatureComponent]
})
export class HeaderAttentionDetailComponent {
	Position = Position;
	@Input() set setDocumentId(documentId: number) {
		if (documentId)
			this.interveningProfessionalsAndStatus$ = this.documentServices.getDocumentElectronicSignatureProfessionalStatus(documentId);
	};

	@Input() title: string;
	@Input() details: Detail[];

	interveningProfessionalsAndStatus$: Observable<DocumentElectronicSignatureProfessionalStatusDto[]>

	constructor(private documentServices: JointSignatureService) {	}

}
