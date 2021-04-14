import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@presentation/components/content-title/content-title.component';
import { Page } from '@presentation/components/page/page.component';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { MenuService } from '../../services/menu.service';
import { PageService } from '../../services/page.service';

const mapToTitle = ({icon, label}): Title => ({icon, label});

class RoutedExtensionComponent {
	extension$: Observable<{title: Title, page: Page}>;

	constructor(
		activatedRoute: ActivatedRoute,
		mapper: (menuData: {menuItemId: string, id: any}) => Observable<{title: Title, page: Page}>,
	) {
		// el id está en el parent y cambiaría junto con este
		const id = activatedRoute.parent.snapshot.paramMap.get( 'id' );
		this.extension$ = activatedRoute.paramMap.pipe(
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
		menuService: MenuService,
		moduleService: PageService,
	) {
		super(activatedRoute, ({menuItemId}) => menuService.getSystemMenuItem(menuItemId).pipe(
			switchMap(menuItem => moduleService.getSystemPage(menuItem.id).pipe(
				map(page => ({
					title: mapToTitle(menuItem),
					page
				}))
			))
		));
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
		menuService: MenuService,
		moduleService: PageService,
	) {
		super(activatedRoute, ({menuItemId, id}) => menuService.getInstitutionMenuItem(id, menuItemId).pipe(
			switchMap(menuItem => moduleService.getInstitutionPage(id, menuItem.id).pipe(
				map(page => ({
					title: mapToTitle(menuItem),
					page
				}))
			))
		));
	}

}
