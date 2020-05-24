import { HttpClient } from "@angular/common/http";
import { TranslateLoader } from "@ngx-translate/core";
import { Observable } from "rxjs";
import { switchMap } from "rxjs/operators";
import { ITranslationResource, MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
import { PublicInfoDto } from '@api-rest/api-model';

export class FlavoredMultiTranslateHttpLoader implements TranslateLoader {
	private translateLoader$: Observable<MultiTranslateHttpLoader>;

	constructor(
		http: HttpClient,
		publicInfo$: Observable<PublicInfoDto>,
		resources: ITranslationResource[],
	) {
		this.translateLoader$ = new Observable((observer) => {
			publicInfo$.subscribe(
				(publicInfo: PublicInfoDto) => {
					observer.next(new MultiTranslateHttpLoader(
						http,
						[
							...resources,
							{ prefix: `./assets/flavors/${publicInfo.flavor}/`, suffix: '.json' }
						]
					));
					observer.complete();
				}
			)
		});
	}

	public getTranslation(lang: string): Observable<any> {
		return this.translateLoader$.pipe(
			switchMap(translateLoader => translateLoader.getTranslation(lang))
		);
	}
}
