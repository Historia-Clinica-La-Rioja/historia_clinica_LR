import { UserPersonDto } from "../api-model";

export const mapToFullName = (userPerson: UserPersonDto): string => [userPerson?.firstName, userPerson?.lastName].filter(item => !!item).join(' ');
