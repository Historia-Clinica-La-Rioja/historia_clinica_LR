import { Component, OnInit } from '@angular/core';
import { SettingsService } from './../../../api-rest/services/settings.service';

@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

	constructor(
		private settingsService: SettingsService,
	) { }

	ngOnInit(): void {
	}

	selectFile(file: File, fileName: string): void {
		this.settingsService.uploadFile(fileName, file).subscribe(data => {

		})
	}

	restore(fileName: string): void {
		this.settingsService.deleteFile(fileName).subscribe(data => {

		});
	}
}
