import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { URL_DOCUMENTS_SIGNATURE } from '../../../../routes/home/home.component';
import { AppFeature, ElectronicSignatureInvolvedDocumentDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { ItemListCard } from '@presentation/components/selectable-card/selectable-card.component';
import { buildItemListCard } from '../../mappers/joint-signature.mapper';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	buttonBack = false;
	routePrefix: string;
	headerInformation: Detail[] = [];
	isLoading: boolean;
	documents: ItemListCard[] = [];
	jointSignatureDocuments: ElectronicSignatureInvolvedDocumentDto[];
	selectedDocumentId: number;

	constructor(
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly jointSignatureService: JointSignatureService
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/'
		this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabledDigital =>{
			this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabledConjunta =>{
			  if(isEnabledConjunta && isEnabledDigital){
				this.buttonBack = true;
			  }
			})
		  })
	}

	ngOnInit(): void {
		this.isLoading = true;
		this.setDocuments();
	}

	goToBackDocumentsSignature(): void {
		this.router.navigate([`${this.routePrefix}${URL_DOCUMENTS_SIGNATURE}`]);
	}

	setDocuments(): void {
		this.jointSignatureService.getProfessionalInvolvedDocumentListPort().subscribe(result => {
			this.jointSignatureDocuments = result;
			this.documents = buildItemListCard(this.jointSignatureDocuments);
			this.isLoading = false;
		})
	}
}
