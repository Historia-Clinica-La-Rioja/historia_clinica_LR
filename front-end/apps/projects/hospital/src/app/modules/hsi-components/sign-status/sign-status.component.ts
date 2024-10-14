import { Component, Input, OnInit } from '@angular/core';
import { EElectronicSignatureStatus } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

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
	selector: 'app-sign-status',
	templateUrl: './sign-status.component.html',
	styleUrls: ['./sign-status.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class SignStatusComponent implements OnInit {

	@Input() signStatus: EElectronicSignatureStatus;

	signOption: ColoredIconText;

	constructor() {
	}

	ngOnInit(): void {
		this.signOption = SIGNATURE_STATUS_OPTION[this.signStatus];
	}

}
