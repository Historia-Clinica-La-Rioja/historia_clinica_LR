import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, concat, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { UIPageDto } from '@extensions/extensions-model';

import { PageService } from '../../services/page.service';

class RoutedExtensionComponent {
	page$: Observable<UIPageDto>;

	constructor(
		activatedRoute: ActivatedRoute,
		mapper: (menuData: { menuItemId: string, id: any }) => Observable<UIPageDto>,
	) {
		// el id está en el parent y cambiaría junto con este
		const id = activatedRoute.parent.snapshot.paramMap.get('id');
		this.page$ = activatedRoute.paramMap.pipe(
			map(params => ({ menuItemId: params.get('menuItemId'), id })),
			switchMap(menuData => concat(of(undefined), mapper(menuData))),
		);
	}

}

@Component({
	selector: 'app-system-extension',
	templateUrl: './extension.component.html',
	styleUrls: ['./extension.component.scss']
})
export class SystemExtensionComponent extends RoutedExtensionComponent {
	constructor(
		activatedRoute: ActivatedRoute,
		extensionSystemService: PageService,
	) {
		super(activatedRoute, ({ menuItemId }) => extensionSystemService.getSystemPage(menuItemId));
	}

}


@Component({
	selector: 'app-institution-extension',
	templateUrl: './extension.component.html',
	styleUrls: ['./extension.component.scss']
})
export class InstitutionExtensionComponent extends RoutedExtensionComponent {
	constructor(
		activatedRoute: ActivatedRoute,
		moduleService: PageService,
	) {
		super(activatedRoute, ({ menuItemId, id }) => moduleService.getInstitutionPage(id, menuItemId));
	}

}
