import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, EMPTY, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { Slot, WCExtensionsService } from '../../services/wc-extensions.service';
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

	getWCParamsFrom(slot: Slot, wcId: string, institutionId: number): Observable<WCParams> {
		if (slot === Slot.HOME_MENU) {
			return this.wCExtensionsService.getSystemHomeMenuPage(wcId);
		}
		console.warn(`El slot ${slot} no se puede usar ruteado`);
		return EMPTY;
	}

	ngOnInit(): void {
		const data$ = this.activatedRoute.data;
		const params$ = this.activatedRoute.paramMap;
		combineLatest([data$, params$]).pipe(
			switchMap(([data, params]) => {
				const wcId = params.get('wcId');
				const slot: Slot = data["slot"];
				return this.getWCParamsFrom(slot, wcId, 1);
			})
		).subscribe(extension => this.extension = extension);
	}


}
