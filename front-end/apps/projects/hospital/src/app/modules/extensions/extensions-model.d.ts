/* tslint:disable */
/* eslint-disable */

export interface ChartDefinitionDto {
    chartType: string;
    cubeQuery: any;
    pivotConfig: any;
}

export interface ExtensionComponentDto {
    name: string;
    path: string;
}

export interface UIComponentDto {
    args: { [index: string]: any };
    type: string;
}

export interface UILabelDto {
    key?: string;
    text?: string;
}

export interface UIMenuItemDto {
    icon: string;
    id: string;
    label: UILabelDto;
}

export interface UIPageDto {
    content: UIComponentDto[];
    layout: string;
}
