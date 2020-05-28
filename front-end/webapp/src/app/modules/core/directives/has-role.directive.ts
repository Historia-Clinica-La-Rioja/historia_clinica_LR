import { Directive, ElementRef, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { PermissionsService } from '@core/services/permissions.service';
import { ERole } from '@api-rest/api-model';
import { anyMatch } from '@core/utils/array.utils';
import { createViewIf } from "@core/utils/directive.utils";

@Directive({
  selector: '[hasRole]'
})
export class HasRoleDirective {

	constructor(
		private element: ElementRef,
		private templateRef: TemplateRef<any>,
		private viewContainer: ViewContainerRef,
		private permissionsService: PermissionsService
	) {
	}

	@Input()
	set hasRole(allowedRoles: ERole[]) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			createViewIf(anyMatch<ERole>(userRoles, allowedRoles));
		});
	}


}
