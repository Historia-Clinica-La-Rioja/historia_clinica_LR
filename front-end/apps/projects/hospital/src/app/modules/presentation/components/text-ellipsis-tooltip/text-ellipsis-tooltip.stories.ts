import { type Meta, type StoryObj } from '@storybook/angular';
import { TextEllipsisTooltipComponent } from './text-ellipsis-tooltip.component';

const meta: Meta<TextEllipsisTooltipComponent> = {
	title: 'Presentation/TextEllipsisTooltipComponent',
	component: TextEllipsisTooltipComponent,
	tags: ['autodocs'],
	parameters: {
		docs: {
			description: {
				component: "Para que funcione correctamente el componente se le tiene que indicar cuanto espacio debe ocupar."
			}
		}
	}
};

export default meta;
type Story = StoryObj<TextEllipsisTooltipComponent>;

export const shortText: Story = {
	args: {
		text: 'Texto corto',
	}
}

export const largeWord: Story = {
	args: {
		text: "texto largooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo",
	},
};

export const largeText: Story = {
	args: {
		text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
	},
};

