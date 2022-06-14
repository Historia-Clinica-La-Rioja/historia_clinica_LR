import { DateTimeDto, LoggedPersonDto } from '@api-rest/api-model';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';

export const mapToFullName = (userPerson: LoggedPersonDto, nameSelfDeterminationFF: boolean): string => [nameSelfDeterminationFF && userPerson?.nameSelfDetermination ? ([userPerson?.nameSelfDetermination, userPerson?.lastName].filter(item => !!item).join(' ')) : ([userPerson?.firstName, userPerson?.lastName].filter(item => !!item).join(' '))].toString();

export const mapToUserInfo = (email: string, userPerson: LoggedPersonDto, nameSelfDeterminationFF: boolean, previousLogin: DateTimeDto): UserInfo => ({
	userName: email,
	fullName: mapToFullName(userPerson, nameSelfDeterminationFF),
	avatar: userPerson.avatar,
	previousLogin: previousLogin,
});
