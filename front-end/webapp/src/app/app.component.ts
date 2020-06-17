import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';

import { PublicInfoDto } from '@api-rest/api-model';
import { PublicService } from '@api-rest/services/public.service';

const DEFAULT_LANG = 'es-AR';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	public publicInfo$: Observable<PublicInfoDto>;

	constructor(
		translate: TranslateService,
		titleService: Title,
		publicService: PublicService,
	) {
		translate.setDefaultLang(DEFAULT_LANG);
		translate.use(DEFAULT_LANG);

		this.publicInfo$ = publicService.getInfo();

		translate.onLangChange.subscribe(() => {
			// Change page title when user changes language preference
			translate.get('app.TITLE').subscribe((res: string) => {
				titleService.setTitle(res);
			});
		});

	}

}
