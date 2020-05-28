
export function getElementViewFunction(viewContainer, templateRef): (boolean) => void {
	return (condition: boolean) => {
		if (condition) {
			viewContainer.createEmbeddedView(templateRef);
		} else {
			viewContainer.clear();
		}
	}
}
