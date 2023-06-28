import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { Observable, combineLatest, switchMap } from 'rxjs';

@Component({
	selector: 'app-institution-routed-external',
	templateUrl: './institution-routed-external.component.html',
	styleUrls: ['./institution-routed-external.component.scss']
})
export class InstitutionRoutedExternalComponent implements OnInit {

	extension: WCParams;

	constructor(
		private readonly activatedRoute: ActivatedRoute,
		private readonly wCExtensionsService: WCExtensionsService
	) { }

	getWCParamsFrom(wcId: string, institutionId: number): Observable<WCParams> {
		return this.wCExtensionsService.getInstitutionMenuPage(wcId, institutionId)
	}

	ngOnInit(): void {
		const params$ = this.activatedRoute.paramMap;
		const parentParams$ = this.activatedRoute.parent.paramMap;
		combineLatest([params$, parentParams$]).pipe(
			switchMap(([params, parentParams]) => {
				const wcId = params.get('wcId');
				const institutionId = Number(parentParams.get('id'))
				return this.getWCParamsFrom(wcId, institutionId);
			})
		).subscribe(extension => this.extension = extension);
	}


}
