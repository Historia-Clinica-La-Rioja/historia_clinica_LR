import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { Page } from '@presentation/components/page/page.component';
import { PageService } from '../../services/page.service';

class RoutedExtensionComponent {
	page$: Observable<Page>;

	constructor(
		activatedRoute: ActivatedRoute,
		mapper: (menuData: {menuItemId: string, id: any}) => Observable<Page>,
	) {
		// el id está en el parent y cambiaría junto con este
		const id = activatedRoute.parent.snapshot.paramMap.get( 'id' );
		this.page$ = activatedRoute.paramMap.pipe(
			map(params => ({menuItemId: params.get('menuItemId'), id })),
			switchMap(menuData => mapper(menuData))
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
		super(activatedRoute, ({menuItemId}) => extensionSystemService.getSystemPage(menuItemId));
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
		super(activatedRoute, ({menuItemId, id}) => moduleService.getInstitutionPage(id, menuItemId));
	}

}
