import { InjectionToken } from "@angular/core";

export const WINDOW =  new InjectionToken('window object',{
    factory: () => window
})