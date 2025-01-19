import { Meta, StoryObj } from "@storybook/angular";
import { OpenStorybookDialogComponent } from "@presentation/components/open-storybook-dialog/open-storybook-dialog.component";
import { ConfirmDialogV2Component } from "./confirm-dialog-v2.component";
import { DialogWidth } from "@presentation/services/dialog.service";

const meta: Meta<OpenStorybookDialogComponent<ConfirmDialogV2Component>> = {
    title: 'Presentation/ConfirmDialogV2Component',
    component: OpenStorybookDialogComponent,
    tags: ['autodocs'],
}

export default meta;

type Story = StoryObj<OpenStorybookDialogComponent<ConfirmDialogV2Component>>;

export const completeLargeConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.LARGE,
            title: 'Ejemplo de dialogo completo con todos los campos posibles',
            hasIcon: true,
            content: 'Este ejemplo contiene todas las modificaciones posibles para este dialogo',
            contentBold: '¿Esta seguro que desea seguir con este paso?',
            okButtonLabel: 'Deseo continuar al siguiente paso',
            cancelButtonLabel: 'No deseo continuar',
            color: 'accent',
            buttonClose: true
        }
    }
}

export const simpleMediumConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.MEDIUM,
            hasIcon: true,
            content: 'Ejemplo de dialogo simple y tamaño mediano'
        }
    }
}

export const warningSmallConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.SMALL,
            hasIcon: true,
            content: 'Ejemplo de dialogo tipo warning',
            contentBold: 'Mensaje resaltado',
            color: 'warn'
        }
    }
}