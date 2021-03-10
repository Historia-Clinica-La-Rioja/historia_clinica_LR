import { Component, OnInit } from '@angular/core';
import { SettingsService } from './../../../api-rest/services/settings.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

const SPONSOR_LOGO = 'sponsor-logo-512x128.png';
const FAVICON = 'favicon.ico';
const ICON_72 = 'icons/icon-72x72.png';
const ICON_96 = 'icons/icon-96x96.png';
const ICON_128 = 'icons/icon-128x128.png';
const ICON_144 = 'icons/icon-144x144.png';
const ICON_152 = 'icons/icon-152x152.png';
const ICON_192 = 'icons/icon-192x192.png';
const ICON_384 = 'icons/icon-384x384.png';
const ICON_512 = 'icons/icon-512x512.png';
const NAME_SPONSOR_LOGO = 'logotipo de auspiciante';
const NAME_FAVICON = 'favicon';
const NAME_ICON_72 = 'icono de 72 x 72 pixeles';
const NAME_ICON_96 = 'icono de 96 x 96 pixeles';
const NAME_ICON_128 = 'icono de 128 x 128 pixeles';
const NAME_ICON_144 = 'icono de 144 x 144 pixeles';
const NAME_ICON_152 = 'icono de 152 x 152 pixeles';
const NAME_ICON_192 = 'icono de 192 x 192 pixeles';
const NAME_ICON_384 = 'icono de 384 x 384 pixeles';
const NAME_ICON_512 = 'icono de 512 x 512 pixeles';
@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

	readonly SPONSOR_LOGO = SPONSOR_LOGO;
	readonly FAVICON = FAVICON;
	readonly ICON_72 = ICON_72;
	readonly ICON_96 = ICON_96;
	readonly ICON_128 = ICON_128;
	readonly ICON_144 = ICON_144;
	readonly ICON_152 = ICON_152;
	readonly ICON_192 = ICON_192;
	readonly ICON_384 = ICON_384;
	readonly ICON_512 = ICON_512;
	readonly NAME_SPONSOR_LOGO = NAME_SPONSOR_LOGO;
	readonly NAME_FAVICON = NAME_FAVICON;
	readonly NAME_ICON_72 = NAME_ICON_72;
	readonly NAME_ICON_96 = NAME_ICON_96;
	readonly NAME_ICON_128 = NAME_ICON_128;
	readonly NAME_ICON_144 = NAME_ICON_144;
	readonly NAME_ICON_152 = NAME_ICON_152;
	readonly NAME_ICON_192 = NAME_ICON_192;
	readonly NAME_ICON_384 = NAME_ICON_384;
	readonly NAME_ICON_512 = NAME_ICON_512;
	readonly BASE_PATH = 'assets/custom/';
	timestamp: string;

	constructor(
		private dialog: MatDialog,
		private settingsService: SettingsService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.timestamp  = this.getTimestamp();
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

	private getTimestamp() {
		return '?ts=' + new Date().getTime();
	}
}
