import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { ContextService } from '@core/services/context.service';

import { SIDEBAR_MENU } from './constants/menu';
import { PermissionsService } from '../core/services/permissions.service';
import { MenuFooter } from '@presentation/components/main-layout/main-layout.component';
import { InstitutionService } from '@api-rest/services/institution.service';
import { InstitutionDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { mapToFullName } from '@api-rest/mapper/user-person-dto.mapper';
import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';
import { MenuService } from '../extensions/services/menu.service';

@Component({
	selector: 'app-institucion',
	templateUrl: './institucion.component.html',
	styleUrls: ['./institucion.component.scss']
})
export class InstitucionComponent implements OnInit {
	menuItems$: Observable<MenuItem[]>;
	menuFooterItems: MenuFooter = {user: {}, institution: null};

	constructor(
		private activatedRoute: ActivatedRoute,
		private contextService: ContextService,
		private extensionMenuService: MenuService,
		private permissionsService: PermissionsService,
		private institutionService: InstitutionService,
		private accountService: AccountService,
		private featureFlagService: FeatureFlagService
	) {

	}

	ngOnInit(): void {
		this.activatedRoute.paramMap.subscribe(params => {
			const institutionId = Number(params.get('id'));
			this.contextService.setInstitutionId(institutionId);

			this.menuItems$ = this.featureFlagService.filterItems$(SIDEBAR_MENU)
				.pipe(
					switchMap(menu => this.permissionsService.filterItems$(menu)),
					map(menuItems => menuItems.map(defToMenuItem)),
					switchMap(items => this.extensionMenuService.getInstitutionMenu(institutionId).pipe(
						map(extesionItems => [...items, ...extesionItems]),
					)),
				);

			this.institutionService.getInstitutions(Array.of(institutionId))
				.subscribe(institutionDto => {
					this.menuFooterItems.institution = {name: institutionDto[0].name, address: this.mapToAddress(institutionDto[0])};
				});
			this.accountService.getInfo()
				.subscribe( userInfo => {
					this.menuFooterItems.user.userName = userInfo.email;
					this.menuFooterItems.user.fullName = mapToFullName(userInfo.personDto);
				}
			);
		});
	}

	private mapToAddress(institutionDto: InstitutionDto) {
		return {
			street: institutionDto.institutionAddressDto.street,
			number: institutionDto.institutionAddressDto.number,
			floor: institutionDto.institutionAddressDto.floor,
			apartment: institutionDto.institutionAddressDto.apartment,
			cityName:  institutionDto.institutionAddressDto.city.description
		};
	}

}
