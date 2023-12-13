import { ActivatedRouteSnapshot } from "@angular/router"

const getParamParent = (route: ActivatedRouteSnapshot, paramName: string): string =>
	route.parent ? getParam(route.parent, paramName) : undefined

export const getParam = (route: ActivatedRouteSnapshot, paramName: string): string =>
	route.paramMap.get(paramName) || getParamParent(route, paramName)