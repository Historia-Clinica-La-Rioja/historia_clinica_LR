import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { getElementViewFunction } from '@core/utils/directive.utils';

@Directive({
	selector: '[appHasRoleWithoutContext]'
})
export class HasRoleWithoutContextDirective {

	private showElement: (showElement: boolean) => void;

	constructor(
		templateRef: TemplateRef<any>,
		viewContainer: ViewContainerRef,
		private readonly permissionsService: PermissionsService
	) {
		this.showElement = getElementViewFunction(viewContainer, templateRef);
	}

	@Input()
	set appHasRoleWithoutContext(allowedRoleNames: string[]) {
		const allowedRoles: ERole[] = <ERole[]>allowedRoleNames;
		
		this.permissionsService.assignmentsWithoutContext$().subscribe((userRoles: ERole[]) =>
			this.showElement(anyMatch<ERole>(userRoles, allowedRoles))
		);
	}

}
