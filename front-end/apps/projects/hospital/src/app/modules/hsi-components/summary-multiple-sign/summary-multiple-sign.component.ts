import { Component, Input, OnInit } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';
import { SummaryAttentionComponent, SummaryAttentionData } from '../summary-attention/summary-attention.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { Color } from '@presentation/colored-label/colored-label.component';
import { EElectronicSignatureStatus } from '@api-rest/api-model';

const SIGNATURE_STATUS_OUTDATED: ColoredIconText = {
	text: 'summary-multiple-sign.state-signature.OUTDATED',
	color: Color.GREY,
	icon: "cancel"
}

const SIGNATURE_STATUS_PENDING: ColoredIconText = {
	text: 'summary-multiple-sign.state-signature.PENDING',
	color: Color.YELLOW,
	icon: "timer"
}

const SIGNATURE_STATUS_REJECTED: ColoredIconText = {
	text: 'summary-multiple-sign.state-signature.REJECTED',
	color: Color.RED,
	icon: "cancel"
}

const SIGNATURE_STATUS_SIGNED: ColoredIconText = {
	text: 'summary-multiple-sign.state-signature.SIGNED',
	color: Color.GREEN,
	icon: "check_circle"
}

const SIGNATURE_STATUS_OPTION = {
	[EElectronicSignatureStatus.OUTDATED]: SIGNATURE_STATUS_OUTDATED,
	[EElectronicSignatureStatus.PENDING]: SIGNATURE_STATUS_PENDING,
	[EElectronicSignatureStatus.REJECTED]: SIGNATURE_STATUS_REJECTED,
	[EElectronicSignatureStatus.SIGNED]: SIGNATURE_STATUS_SIGNED
};

@Component({
	selector: 'app-summary-multiple-sign',
	templateUrl: './summary-multiple-sign.component.html',
	styleUrls: ['./summary-multiple-sign.component.scss'],
	standalone: true,
	imports: [PresentationModule, SummaryAttentionComponent]
})

export class SummaryMultipleSignComponent implements OnInit {

	@Input() data: SummaryMultipleSignData;

	signStatus: ColoredIconText;

	constructor() {
	}

	ngOnInit(): void {
		this.signStatus = SIGNATURE_STATUS_OPTION[this.data.signStatus];
	}

}

export interface SummaryMultipleSignData {
	attentionData: SummaryAttentionData,
	signStatus?: EElectronicSignatureStatus
}
