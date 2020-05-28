import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { getElementViewFunction } from "@core/utils/directive.utils";

@Directive({
	selector: '[featureFlag]'
})
export class FeatureFlagDirective {

	private showElement: (boolean) => void;

	constructor(
		templateRef: TemplateRef<any>,
		viewContainer: ViewContainerRef,
		private featureFlagService: FeatureFlagService) {
		this.showElement = getElementViewFunction(viewContainer, templateRef);
	}


	@Input()
	set featureFlag(featureFlag: string) {
		this.featureFlagService.isOn(featureFlag).subscribe(isOn => {
			this.showElement(isOn);
		});

	}
}
