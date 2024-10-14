import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap, take } from 'rxjs/operators';

import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

import { InstitutionService } from '@api-rest/services/institution.service';
import { AccountService } from '@api-rest/services/account.service';

import { MenuItem, defToMenuItem } from '@presentation/components/menu/menu.component';
import { LocationInfo } from '@presentation/components/location-badge/location-badge.component';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';
import { mapToUserInfo } from '@api-presentation/mappers/user-person-dto.mapper';
import { mapToLocation } from '@api-presentation/mappers/institution-dto.mapper';

import { MenuService } from '@extensions/services/menu.service';

import { HomeRoutes } from '../home/constants/menu';
import { AppRoutes } from '../../app-routing.module';
import { SIDEBAR_MENU } from './constants/menu';
import { AppFeature} from "@api-rest/api-model";
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';

@Component({
	selector: 'app-institucion',
	templateUrl: './institucion.component.html',
	styleUrls: ['./institucion.component.scss']
})
export class InstitucionComponent implements OnInit {
	userProfileLink = ['/', AppRoutes.Home, HomeRoutes.Profile];
	institutionHomeLink: any[];
	menuItems$: Observable<MenuItem[]>;
	institution: LocationInfo;
	userInfo: UserInfo;
	roles = [];
	nameSelfDeterminationFF: boolean;

	constructor(
		private activatedRoute: ActivatedRoute,
		private contextService: ContextService,
		private extensionMenuService: MenuService,
		private permissionsService: PermissionsService,
		private institutionService: InstitutionService,
		private accountService: AccountService,
		private featureFlagService: FeatureFlagService,
		private readonly wcExtensionsService: WCExtensionsService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {

		this.activatedRoute.paramMap.pipe(take(1)).subscribe(params => {
			const institutionId = Number(params.get('id'));
			this.contextService.setInstitutionId(institutionId);
			this.institutionHomeLink = ['/', AppRoutes.Institucion, this.contextService.institutionId];

			this.menuItems$ = this.featureFlagService.filterItems$(SIDEBAR_MENU)
				.pipe(
					switchMap(menu => this.permissionsService.filterItems$(menu)),
					map(menuItems => menuItems.map(defToMenuItem)),
					switchMap(items => this.extensionMenuService.getInstitutionMenu(institutionId).pipe(
						map(extesionItems => [...items, ...extesionItems]),
					)),
					switchMap(items => this.wcExtensionsService.getInstitutionMenu().pipe(map(extensiones => [...items, ...extensiones])))
				);

			this.institutionService.getInstitutions(Array.of(institutionId))
				.subscribe(institutionDto =>
					this.institution = mapToLocation(institutionDto[0])
				);
			this.accountService.getInfo()
				.subscribe(userInfo => {
					this.userInfo = mapToUserInfo(userInfo.email, userInfo.personDto, this.nameSelfDeterminationFF, userInfo.previousLogin)
				})
		});
		this.permissionsService.contextRoleAssignments$.subscribe(
			roles => this.roles = roles.map(role => role.roleDescription)
		);

	}


}
