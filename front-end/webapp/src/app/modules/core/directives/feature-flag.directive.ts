import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { createViewIf } from "@core/utils/directive.utils";

@Directive({
	selector: '[featureFlag]'
})
export class FeatureFlagDirective {

	constructor(
		private templateRef: TemplateRef<any>,
		private viewContainer: ViewContainerRef,
		private featureFlagService: FeatureFlagService) {
	}


	@Input()
	set featureFlag(featureFlag: string) {
		this.featureFlagService.isOn(featureFlag).subscribe(isOn => {
			createViewIf(isOn);
		});

	}
}
