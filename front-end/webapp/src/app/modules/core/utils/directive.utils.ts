
export function createViewIf(condition: boolean): void {
	if (condition) {
		this.viewContainer.createEmbeddedView(this.templateRef);
	} else {
		this.viewContainer.clear();
	}
}
