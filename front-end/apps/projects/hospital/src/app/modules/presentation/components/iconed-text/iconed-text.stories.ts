import { type Meta, type StoryObj } from '@storybook/angular';
import { IconedTextComponent } from './iconed-text.component';

const meta: Meta<IconedTextComponent> = {
    title: 'Presentation/IconedTextComponent',
    component: IconedTextComponent,
    tags: ['autodocs'],
    argTypes: {
		icon: {
			options: ["swap_horiz", "medical_services", "domain", "error_outline", "library_add"],
			control: {
				type: 'select',
			},
			description: 'Usar mat-icon'
		}
	}
};

export default meta;

type Story = StoryObj<IconedTextComponent>;

export const full: Story = {
    args: {
        icon: "library_add",
        text: "texto de ejemplo"
    }
};

