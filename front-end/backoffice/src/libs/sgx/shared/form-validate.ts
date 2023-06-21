import { regex } from "react-admin";

const validateUrl = regex(/^(https?:\/\/)([^\s/$.?#].[^\s]*)$/, 'Debe ser una url válida');

export { validateUrl };