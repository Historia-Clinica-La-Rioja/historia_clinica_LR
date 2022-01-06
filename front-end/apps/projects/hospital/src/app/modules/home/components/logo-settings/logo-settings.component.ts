import { Component, OnInit } from '@angular/core';
import { SettingsService } from '@api-rest/services/settings.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Asset } from './asset.model';
import { addAvailableAssetsToList } from './asset.facade';
import { FaviconCreator, SponsorLogoCreator } from './asset.creator';

@Component({
	selector: 'app-logo-settings',
	templateUrl: './logo-settings.component.html',
	styleUrls: ['./logo-settings.component.scss']
})
export class LogoSettingsComponent implements OnInit {

	readonly BASE_PATH = 'assets/custom/';
	favicon: Asset;
	sponsorLogo: Asset;
	icons: Asset[];
	timestamp: string;

	private assets: Asset[];

	constructor(
		private dialog: MatDialog,
		private settingsService: SettingsService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.timestamp = this.getTimestamp();
		this.assets = addAvailableAssetsToList();
		const faviconInstance = new FaviconCreator().factoryMethod();
		const sponsorLogoInstance = new SponsorLogoCreator().factoryMethod();
		this.favicon = this.assets.find(a => a.name === faviconInstance.getName());
		this.sponsorLogo = this.assets.find(a => a.name === sponsorLogoInstance.getName());
		this.icons = this.assets.filter(a => a.name !== faviconInstance.getName() && a.name !== sponsorLogoInstance.getName());
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
