import { Component, Input, OnInit } from '@angular/core';


@Component({
	selector: 'app-file-download-card',
	templateUrl: './file-download-card.component.html',
	styleUrls: ['./file-download-card.component.scss']
})
export class FileDownloadCardComponent implements OnInit {
	@Input() dialogData: DownloadMessageArgs = {icon: 'downloading', text: ''};

	constructor(
	) { }

	ngOnInit(): void {


	}

}

export interface DownloadMessageArgs {
	icon: string;
	text: string;
}
