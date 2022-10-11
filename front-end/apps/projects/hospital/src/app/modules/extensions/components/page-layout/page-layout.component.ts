import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UIPageDto } from '@extensions/extensions-model';
import { CSVFileDownloadService } from '@extensions/services/csvfile-download.service';

@Component({
	selector: 'app-page-layout',
	templateUrl: './page-layout.component.html',
	styleUrls: ['./page-layout.component.scss']
})
export class PageLayoutComponent {

	@Input() page: UIPageDto = { layout: 'loading', content: [] };

	constructor(
		activatedRoute: ActivatedRoute,
		public fileDownloadService: CSVFileDownloadService,
	) {
		activatedRoute.data.subscribe((data: Boolean) => {
			fileDownloadService.enabledDownload = data.valueOf();
		}
		);
	}

}
