import type { Meta, StoryObj } from '@storybook/angular';
import { BoxMessageComponent } from './box-message.component';

const meta: Meta<BoxMessageComponent> = {
    title: 'Historia Clinica/BoxMessageComponent',
    component: BoxMessageComponent,
    tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<BoxMessageComponent>;

export const noError: Story = {
    args: {
        boxMessageInfo: {
            title: 'Aviso',
            question: '¿Desea incluirlo en la consulta?',
            message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
            viewError: false,
            showButtons: true
        }
        
    }
};

export const hasError: Story = {
    args: {
        boxMessageInfo: {
            title: 'Aviso',
            question: '¿Desea incluirlo en la consulta?',
            message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
            viewError: true,
            showButtons: true
        }
    }
};

export const basicMessage: Story = {
    args: {
        boxMessageInfo: {
            message: 'Este tipo de solicitud pasará por un proceso de aprobación previo a ser asignado.',
            showButtons: false
        }
    }
}

export const customLabelButtons: Story = {
    args: {
        boxMessageInfo: {
            title: 'Aviso',
            question: '¿Desea incluirlo en la consulta?',
            message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
            viewError: false,
            addButtonLabel: 'Si, incuir referencia',
            discardButtonLabel: 'No, descartar',
            showButtons: true
        }
    }
};



