import { type Meta, type StoryObj } from '@storybook/angular';
import { ColoredIconTextComponent } from './colored-icon-text.component';
import { Color } from '@presentation/colored-label/colored-label.component';

const meta: Meta<ColoredIconTextComponent> = {
    title: 'Presentation/ColoredIconTextComponent',
    component: ColoredIconTextComponent,
    tags: ['autodocs'],
    argTypes: {
        coloredIconText: {
            description: "Usar mat-icon y enum Color"
        }
    }
};

export default meta;

type Story = StoryObj<ColoredIconTextComponent>;

export const red: Story = {
    args: {
        coloredIconText: {
            icon: "library_add",
            color: Color.RED,
            text: "Ejemplo"
        }
    }
};

export const blue: Story = {
    args: {
        coloredIconText: {
            icon: "library_add",
            color: Color.BLUE,
            text: "Ejemplo"
        }
    }
};

export const green: Story = {
    args: {
        coloredIconText: {
            icon: "library_add",
            color: Color.GREEN,
            text: "Ejemplo"
        }
    }
};

export const yellow: Story = {
    args: {
        coloredIconText: {
            icon: "library_add",
            color: Color.YELLOW,
            text: "Ejemplo"
        }
    }
};

export const grey: Story = {
    args: {
        coloredIconText: {
            icon: "library_add",
            color: Color.GREY,
            text: "Ejemplo"
        }
    }
};

