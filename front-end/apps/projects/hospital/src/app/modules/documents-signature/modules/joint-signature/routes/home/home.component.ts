import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { URL_DOCUMENTS_SIGNATURE } from '../../../../routes/home/home.component';
import { AppFeature, EElectronicSignatureStatus, ElectronicSignatureInvolvedDocumentDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { ItemListCard, ItemListOption, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { getDocumentTypeByEnum } from '@core/constants/summaries';
import { convertDateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { Color } from '@presentation/colored-label/colored-label.component';
import { dateTimeToViewDateHourMinuteSecond, dateToViewDate } from '@core/utils/date.utils';

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
		private readonly jointSignatureService: JointSignatureService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/'
		this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabledDigital => {
			this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabledConjunta => {
				if (isEnabledConjunta && isEnabledDigital) {
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
			this.buildItemListCard();
			this.isLoading = false;
		})
	}

	private buildItemListCard(): void {
		this.documents = this.jointSignatureDocuments.map(document => {
			return {
				id: document.documentId,
				icon: getDocumentTypeByEnum(document.documentType).icon,
				title: getDocumentTypeByEnum(document.documentType).title,
				options: this.buildItemListOption(document),
				coloredIconTextOption: this.buildSignatureStatusOption(document.signatureStatus)
			}
		})
	}

	private buildItemListOption(document: ElectronicSignatureInvolvedDocumentDto): ItemListOption[] {
		return [
			{
				title: 'digital-signature.card-information.PROBLEM',
				value: document.problems.length ? this.buildValues(document.problems) : ['digital-signature.card-information.NO_SNOMED_CONCEPT']
			},
			{
				title: 'digital-signature.card-information.CREATED',
				value: [dateTimeToViewDateHourMinuteSecond(convertDateTimeDtoToDate(document.documentCreationDate))],
			},
			{
				title: 'digital-signature.card-information.PROFESSIONAL',
				value: [document.responsibleProfessionalCompleteName]
			},
			{
				title: 'digital-signature.card-information.PATIENT',
				value: [document.patientCompleteName],
			}
		]
	}

	private buildValues(problems: string[]) {
		if (problems.length > 1)
			return [problems[0] + ' (+' + (problems.length - 1).toString() + ' más)'];
		return [problems[0]];
	}

	private buildSignatureStatusOption(status: string): ItemListOption {
		return {
			title: 'digital-signature.card-information.SIGN_STATUS',
			value: this.buildSignatureStatusValue(status)
		}
	}

	private buildSignatureStatusValue(status: string): ColoredIconText {
		switch (status) {
			case EElectronicSignatureStatus.OUTDATED:
				return {
					text: 'firma-conjunta.STATE_SIGNATURE.OUTDATED',
					color: Color.GREY,
					icon: "cancel"
				}
			case EElectronicSignatureStatus.PENDING:
				return {
					text: 'firma-conjunta.STATE_SIGNATURE.PENDING',
					color: Color.YELLOW,
					icon: "timer"
				}
			case EElectronicSignatureStatus.REJECTED:
				return {
					text: 'firma-conjunta.STATE_SIGNATURE.REJECTED',
					color: Color.RED,
					icon: "cancel"
				}
			case EElectronicSignatureStatus.SIGNED:
				return {
					text: 'firma-conjunta.STATE_SIGNATURE.SIGNED',
					color: Color.GREEN,
					icon: "check_circle"
				}
		}
	}

	private buildHeaderInformation(document: ElectronicSignatureInvolvedDocumentDto): Detail[] {
		return [
			{
				title: 'firma-conjunta.details.AMBIT',
				text: getDocumentTypeByEnum(document.documentType).title
			},
			{
				title: 'firma-conjunta.details.PATIENT',
				text: document.patientCompleteName
			},
			{
				title: 'firma-conjunta.details.DATE',
				text: dateToViewDate(convertDateTimeDtoToDate(document.documentCreationDate))
			},
			{
				title: 'firma-conjunta.details.PROFESSIONAL',
				text: document.responsibleProfessionalCompleteName
			},
		]
	}

	seeDetails(ids: SelectableCardIds) {
		this.selectedDocumentId = ids.id;
		this.headerInformation = this.buildHeaderInformation(this.jointSignatureDocuments.find(item => item.documentId === ids.id));
	}
}
