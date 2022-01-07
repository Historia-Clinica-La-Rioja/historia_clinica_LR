import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { getElementViewFunction } from '@core/utils/directive.utils';

/**
 *  Example:
 *  <ng-container *appFeatureFlag="'habilitarEditarPaciente'">
 *      ...
 *  </ng-container>
 *
 */
@Directive({
	selector: '[appFeatureFlag]'
})
export class FeatureFlagDirective {

	private showElement: (showElement: boolean) => void;

	constructor(
		templateRef: TemplateRef<any>,
		viewContainer: ViewContainerRef,
		private featureFlagService: FeatureFlagService) {
		this.showElement = getElementViewFunction(viewContainer, templateRef);
	}


	@Input()
	set appFeatureFlag(featureFlagName: string) {
		const feature: AppFeature = <AppFeature> featureFlagName;
		this.featureFlagService.isActive(feature).subscribe(isActive => {
			this.showElement(isActive);
		});
	}
}
