import { Component, OnInit } from '@angular/core';
import { SettingsService } from './../../../api-rest/services/settings.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const SPONSOR_LOGO = "sponsor-logo-512x128.png";
const FAVICON = "favicon.ico";
const ICON_72 = "icons/icon-72x72.png";
const ICON_96 = "icons/icon-96x96.png";
const ICON_128 = "icons/icon-128x128.png";
const ICON_144 = "icons/icon-144x144.png";
const ICON_152 = "icons/icon-152x152.png";
const ICON_192 = "icons/icon-192x192.png";
const ICON_384 = "icons/icon-384x384.png";
const ICON_512 = "icons/icon-512x512.png";
@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

	SPONSOR_LOGO = SPONSOR_LOGO;
	FAVICON = FAVICON;
	ICON_72 = ICON_72;
	ICON_96 = ICON_96;
	ICON_128 = ICON_128;
	ICON_144 = ICON_144;
	ICON_152 = ICON_152;
	ICON_192 = ICON_192;
	ICON_384 = ICON_384;
	ICON_512 = ICON_512;

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
