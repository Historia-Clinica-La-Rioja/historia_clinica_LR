import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
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
	set appFeatureFlag(featureFlag: string) {
		this.featureFlagService.isOn(featureFlag).subscribe(isOn => {
			this.showElement(isOn);
		});
	}
}
