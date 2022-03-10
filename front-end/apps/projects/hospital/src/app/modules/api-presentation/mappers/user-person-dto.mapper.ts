import { LoggedPersonDto } from '@api-rest/api-model';
import { UserInfo } from '@presentation/components/user-badge/user-badge.component';

export const mapToFullName = (userPerson: LoggedPersonDto): string => [userPerson?.firstName, userPerson?.lastName].filter(item => !!item).join(' ');

export const mapToUserInfo = (email: string, userPerson: LoggedPersonDto): UserInfo => ({
	userName: email,
	fullName: mapToFullName(userPerson),
	avatar: userPerson.avatar,
});
