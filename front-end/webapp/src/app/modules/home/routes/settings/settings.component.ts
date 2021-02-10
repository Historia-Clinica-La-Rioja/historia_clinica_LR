import { Component, OnInit } from '@angular/core';
import { SettingsService } from './../../../api-rest/services/settings.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

	constructor(
		private settingsService: SettingsService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
	}

	selectFile(file: File, fileName: string): void {
		if (file) {
			this.settingsService.uploadFile(fileName, file).subscribe(data => {
				if (data) {
					this.snackBarService.showSuccess('configuracion.logos.toast_messages.UPDATE_IMAGE_SUCCESS');
				} else {
					this.snackBarService.showError('configuracion.logos.toast_messages.UPDATE_IMAGE_ERROR');
				}
			});
		} else {
			this.snackBarService.showError('configuracion.logos.toast_messages.VALID_IMAGE_ERROR');
		}
	}

	restore(fileName: string): void {
		this.settingsService.deleteFile(fileName).subscribe(data => {
			if (data) {
				this.snackBarService.showSuccess('configuracion.logos.toast_messages.UPDATE_IMAGE_SUCCESS');
			} else {
				this.snackBarService.showError('configuracion.logos.toast_messages.UPDATE_IMAGE_ERROR');
			}
		});
	}
}
