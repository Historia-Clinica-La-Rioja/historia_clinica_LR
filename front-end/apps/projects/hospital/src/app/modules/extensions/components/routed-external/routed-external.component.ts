import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';

import { WCExtensionsService } from '../../services/wc-extensions.service';
import { WCParams } from '../ui-external-component/ui-external-component.component';

@Component({
	selector: 'app-routed-external',
	templateUrl: './routed-external.component.html',
	styleUrls: ['./routed-external.component.scss']
})
export class RoutedExternalComponent implements OnInit {
	extension: WCParams;

	constructor(
		private readonly activatedRoute: ActivatedRoute,
		private readonly wCExtensionsService: WCExtensionsService
	) { }

	ngOnInit(): void {
		const params$ = this.activatedRoute.paramMap;
		params$.pipe(
			switchMap((params) => {
				const wcId = params.get('wcId');
				return this.wCExtensionsService.getSystemHomeMenuPage(wcId);
			})
		).subscribe(extension => this.extension = extension);
	}


}
