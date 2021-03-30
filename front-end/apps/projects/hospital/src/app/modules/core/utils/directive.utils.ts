
export function getElementViewFunction(viewContainer: any, templateRef: any): (show: boolean) => void {
	return (condition: boolean) => {
		if (condition) {
			viewContainer.createEmbeddedView(templateRef);
		} else {
			viewContainer.clear();
		}
	};
}
