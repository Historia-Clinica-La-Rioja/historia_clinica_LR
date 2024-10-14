import { Params } from "@angular/router"
import { ParamsToSearchPerson } from "@pacientes/component/search-create/search-create.component"

export const toParamsToSearchPerson = (params: Params): ParamsToSearchPerson => {
	return {
		...(params.firstName && { firstName: params.firstName}),
		...(params.middleNames && { middleNames: params.middleNames}),
		...(params.lastName && { lastName: params.lastName}),
		...(params.otherLastNames && { otherLastNames: params.otherLastNames}),
		...(params.birthDate && { birthDate: params.birthDate}),
		...(params.identificationTypeId && { identificationTypeId: params.identificationTypeId}),
		...(params.identificationNumber && { identificationNumber: params.identificationNumber}),
		...(params.genderId && { genderId: params.genderId}),
		...(params.identityVerificationStatus && { identityVerificationStatus: params.identityVerificationStatus}),
		...(params.comments && { comments: params.comments}),
		...(params.noIdentity && { noIdentity: params.noIdentity}),
		...(params.typeId && { typeId: params.typeId}),
		...(params.cuil && { cuil: params.cuil}),
		...(params.photo && { photo: params.photo}),
		...(params.fromGuardModule && { fromGuardModule: params.fromGuardModule}),
	}

}

export const encode = (textToEncode: string): string => {
	return btoa(textToEncode);
}

export const decode = (textToDecode: string): string => {
	return atob(textToDecode);
}