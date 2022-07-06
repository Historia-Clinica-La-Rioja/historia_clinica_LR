import { Component, OnInit } from '@angular/core';
import { AuthService } from "@api-rest/services/auth.service";
import { MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-login-pincode',
  templateUrl: './login-pin-code.component.html',
  styleUrls: ['./login-pin-code.component.scss']
})
export class LoginPinCodeComponent implements OnInit {

	readonly VERIFICATION_CODE_LENGTH = 6;

	verificationCode = '';
	verificationCodeTooShort = false;
	verificationCodeInvalid = false;

	constructor(
		public dialogRef: MatDialogRef<LoginPinCodeComponent>,
		private readonly authService: AuthService,
	) { }

	ngOnInit(): void {
	}

	onCodeChange(event) {
		this.verificationCode = event;
	}

	onCodeCompleted(event) {
		this.verificationCodeTooShort = false;
		this.verificationCodeInvalid = false;
	}

	verify() {
		this.verificationCodeInvalid = false;
		if (!this.isCodeValid()) {
			this.verificationCodeTooShort = true;
			return;
		}
		this.authService.completeLoginWith2FA(this.verificationCode).subscribe(res => {
			this.dialogRef.close({ loginSuccessful : true });
		}, error => {
			if (error?.code === "INVALID_CODE") {
				this.verificationCodeInvalid = true;
			}
		});

	}

	private isCodeValid(): boolean {
		return this.verificationCode.length === this.VERIFICATION_CODE_LENGTH;
	}

}
