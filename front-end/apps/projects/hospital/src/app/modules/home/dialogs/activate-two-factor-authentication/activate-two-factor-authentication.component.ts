import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";
import { TwoFactorAuthenticationService } from "@api-rest/services/two-factor-authentication.service";

@Component({
  selector: 'app-activate-two-factor-authentication',
  templateUrl: './activate-two-factor-authentication.component.html',
  styleUrls: ['./activate-two-factor-authentication.component.scss']
})
export class ActivateTwoFactorAuthenticationComponent implements OnInit {

	readonly VERIFICATION_CODE_LENGTH = 6;

	sharedSecret: string;
	sharedSecretBarCode: string;

	firstStepCompleted = false;

	verificationCode = '';
	verificationCodeTooShort = false;
	verificationCodeInvalid = false;

	constructor(
		public dialogRef: MatDialogRef<ActivateTwoFactorAuthenticationComponent>,
		private twoFactorAuthenticationService: TwoFactorAuthenticationService,
	) {}

	ngOnInit(): void {
		this.twoFactorAuthenticationService.generateTwoFactorAuthenticationCodes().subscribe((codes) => {
			this.sharedSecret = codes.sharedSecret;
			this.sharedSecretBarCode = codes.sharedSecretBarCode;
		})
	}

	goToNextStep() {
		this.firstStepCompleted = true;
		this.dialogRef.updateSize('30%');
	}

	goToPreviousStep() {
		this.firstStepCompleted = false;
		this.verificationCodeTooShort = false;
		this.verificationCodeInvalid = false;
		this.dialogRef.updateSize('40%');
	}

	onCodeChange(event) {
		this.verificationCode = event;
	}

	onCodeCompleted(event) {
		this.verificationCodeTooShort = false;
		this.verificationCodeInvalid = false;
	}

	confirm() {
		this.verificationCodeInvalid = false;
		if (!this.isCodeValid()) {
			this.verificationCodeTooShort = true;
			return;
		}
		this.twoFactorAuthenticationService.confirmTwoFactorAuthenticationCode(this.verificationCode)
			.subscribe((confirmed) => {
				if (confirmed) {
					this.dialogRef.close({ confirmed });
					return;
				}
				this.verificationCodeInvalid = true;
			});
	}

	private isCodeValid(): boolean {
		return this.verificationCode.length === this.VERIFICATION_CODE_LENGTH;
	}
}
