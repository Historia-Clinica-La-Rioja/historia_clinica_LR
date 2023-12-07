import { Component, OnInit } from '@angular/core';
import { UserKeysService } from "../../components/user-keys/user-keys.service";
import { MatDialogRef } from "@angular/material/dialog";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { catchError, tap } from "rxjs/operators";
import { GenerateApiKeyDto } from "@api-rest/api-model";
import { EMPTY } from "rxjs";
import { TranslateService } from "@ngx-translate/core";

@Component({
	selector: 'app-generate-api-key-form',
	templateUrl: './generate-api-key-form.component.html',
	styleUrls: ['./generate-api-key-form.component.scss']
})
export class GenerateApiKeyFormComponent implements OnInit {
	public loading: boolean;
	public generated: boolean;
	public secret;
	public isCopied: boolean;
	userKeyToAdd: GenerateApiKeyDto;

	constructor(
		private userKeysService: UserKeysService,
		private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService,
		public dialogRef: MatDialogRef<GenerateApiKeyFormComponent>,
	) {
	}

	ngOnInit(): void {
		this.loading = false;
		this.generated = false;
		this.isCopied = false;
	}

	addKey() {
		// this.addError = undefined;
		// this.userKeysService.add(this.userKeyToAdd).pipe(
		// 	catchError(error => {
		// 		this.addError = error;
		// 		return of(undefined);
		// 	}),
		// ).subscribe(
		// 	apiKeyAdded => this.lastUserKeyAdded = apiKeyAdded
		// );
		// if (this.form.valid) {
		this.loading = true;
		this.userKeysService.add(this.userKeyToAdd).pipe(
			tap((keyGenerated) => {
				this.snackBarService.showSuccess(this.translateService.instant("profile.api-key.generate-dialog.GENERATED_SUCCESSFULLY"));
				this.generated = true;
				this.loading = false;
				this.secret = keyGenerated.apiKey;
			}),
			catchError((_) => {
				this.userKeyToAdd = undefined;
				this.generated = false;
				this.loading = false;
				return EMPTY;
			})
		).subscribe();
		// }
	}

	closeDialog() {
		this.dialogRef.close();
	}

	copied() {
		if (this.generated)
			this.isCopied = true;
		this.snackBarService.showSuccess(this.translateService.instant("messages.COPIED"));
		setTimeout(() => {
			this.isCopied = false;
		}, 300);
	}

	newUserKey(userKey: GenerateApiKeyDto) {
		this.userKeyToAdd = userKey;
	}

}
