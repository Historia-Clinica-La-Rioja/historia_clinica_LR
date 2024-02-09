import type { Meta, StoryObj } from '@storybook/angular';
import { IncludePreviousDataQuestionComponent } from './include-previous-data-question.component';

const meta: Meta<IncludePreviousDataQuestionComponent> = {
    title: 'HSI/IncludePreviousDataQuestionComponent',
    component: IncludePreviousDataQuestionComponent,
    tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<IncludePreviousDataQuestionComponent>;

export const noError: Story = {
    args: {
        question: '¿Desea incluirlo en la consulta?',
        message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
        viewError: false,
    }
};

export const hasError: Story = {
    args: {
        question: '¿Desea incluirlo en la consulta?',
        message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
        viewError: true,
    }
};

export const customLabelButtons: Story = {
    args: {
        question: '¿Desea incluirlo en la consulta?',
        message: 'Se incluyó el problema "Golpes", se recomienda incluir una solicitud de referencia para un equipo especializado en abordaje de violencias.',
        viewError: false,
        addButtonLabel: 'Si, incuir referencia',
        discardButtonLabel: 'No, descartar',
    }
};



