import type { Meta, StoryObj } from '@storybook/angular';
import { TitleDescriptionComponent } from './title-description.component';


const meta: Meta<TitleDescriptionComponent> = {
	title: 'Presentation/TitleDescriptionComponent',
	component: TitleDescriptionComponent,
	tags: ['autodocs'],
	argTypes: {
		title: {
			description: 'Titulo a mostrar',
			control: {
				type: 'text',
			},
		},
		text: {
			description: "Texto a mostrar",
			control: {
				type: 'text',
			},
		},
		showFullText: {
			description: "Indica si el componente tiene que mostrar todo el texto o adaptarlo al espacio disponible",
			control: {
				type: 'boolean',
			},
			defaultValue: true,
		}
	}
};

export default meta;

type Story = StoryObj<TitleDescriptionComponent>;

export const shortTextAndFullText: Story = {
	args: {
		title: "Observaciones",
		text: "Detalle de las observaciones"
	}
};

export const longTextAndFullText: Story = {
	args: {
		title: "Descripcion larga",
		text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
	}
};

export const shortTextAndWithoutFullText: Story = {
	args: {
		title: "Observaciones",
		text: "Detalle de las observaciones",
		showFullText: false,
	}
};

export const longTextAndWithoutFullText: Story = {
	args: {
		title: "Descripcion larga",
		text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
		showFullText: false,
	}
};
