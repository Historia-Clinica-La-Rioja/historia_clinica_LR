import type { Meta, StoryObj } from '@storybook/angular';
import { UserBadgeComponent, UserInfo } from './user-badge.component';


const meta: Meta<UserBadgeComponent> = {
	title: 'Presentation/UserBadgeComponent',
	component: UserBadgeComponent,
	tags: ['autodocs'],
	args: {
		userInfo: { userName: 'Pepe', fullName: 'Jose Argento', avatar: 'https://previews.123rf.com/images/yupiramos/yupiramos1804/yupiramos180411698/99583966-young-man-with-mustache-avatar-character.jpg', previousLogin: { date: { day: 1, month: 6, year: 2024 }, time: { hours: 13, minutes: 20 } } },
		hideName: false
	},
	argTypes: {
		userInfo: {
			userInfo: UserInfo,
			description: "use class UserInfo"
		}
	}
};

export default meta;
type Story = StoryObj<UserBadgeComponent>;

export const Default: Story = {};

export const NoAvatar: Story = {
	args: {
		userInfo: { userName: 'Pepe', fullName: 'Jose Argento', previousLogin: { date: { day: 1, month: 6, year: 2024 }, time: { hours: 13, minutes: 20 } } }
	},
};

export const NoFullName: Story = {
	args: {
		userInfo: { userName: 'Pepe', previousLogin: { date: { day: 1, month: 6, year: 2024 }, time: { hours: 13, minutes: 20 } } }
	},
};

export const HideName: Story = {
	args: {
		hideName: true
	},
};

export const HideNameNoAvatar: Story = {
	args: {
		...HideName.args, ...NoAvatar.args
	},
};
