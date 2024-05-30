import { GlobalCoordinatesDto } from "@api-rest/api-model";

export const transformCoordinates = (coordinates: GlobalCoordinatesDto) => {
    return `${coordinates.latitude}, ${coordinates.longitude}`;
}