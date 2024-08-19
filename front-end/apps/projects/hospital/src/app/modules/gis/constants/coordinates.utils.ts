import { GlobalCoordinatesDto } from "@api-rest/api-model";

export const transformCoordinates = (coordinates: GlobalCoordinatesDto) => {
    if (!coordinates?.latitude) return '';
    return `${coordinates.latitude}, ${coordinates.longitude}`;
}