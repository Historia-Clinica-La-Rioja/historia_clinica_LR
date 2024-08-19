import { Component, Input, OnInit } from '@angular/core';
import { Asset } from '../../routes/appearance/asset.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SettingsService } from '@api-rest/services/settings.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-asset-form',
	templateUrl: './asset-form.component.html',
	styleUrls: ['./asset-form.component.scss']
})
export class AssetFormComponent implements OnInit {
	@Input() icon: Asset;
	@Input() basePath: string;
	timestamp: string;

	constructor(
		private dialog: MatDialog,
		private settingsService: SettingsService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.timestamp = this.getTimestamp();
	}

	selectFile(file: File, fileName: string): void {
		if (file) {
			this.settingsService.uploadFile(fileName, file).subscribe(data => {
				if (data) {
					this.timestamp = this.getTimestamp();
					this.snackBarService.showSuccess('configuracion.logos.toast_messages.UPDATE_IMAGE_SUCCESS');
				} else {
					this.snackBarService.showError('configuracion.logos.toast_messages.UPDATE_IMAGE_ERROR');
				}
			});
		} else {
			this.snackBarService.showError('configuracion.logos.toast_messages.VALID_IMAGE_ERROR');
		}
	}


	restore(fileName: string, fileNameToShow: string): void {
		const dialogRefConfirmation = this.dialog.open(ConfirmDialogComponent,
			{
				data: {
					title: 'configuracion.logos.confirm_dialog.TITLE',
					content: '¿Está seguro que desea restablecer el ' + fileNameToShow + '?',
				}
			});

		dialogRefConfirmation.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.settingsService.deleteFile(fileName).subscribe(data => {
					if (data) {
						this.timestamp = this.getTimestamp();
						this.snackBarService.showSuccess('configuracion.logos.toast_messages.UPDATE_IMAGE_SUCCESS');
					} else {
						this.snackBarService.showError('configuracion.logos.toast_messages.UPDATE_IMAGE_ERROR');
					}
				});
			}
		});
	}

	private getTimestamp(): string {
		return '?ts=' + new Date().getTime();
	}

}
