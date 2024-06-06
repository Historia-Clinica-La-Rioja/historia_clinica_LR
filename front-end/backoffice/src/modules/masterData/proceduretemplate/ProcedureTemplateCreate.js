import React from 'react';
import {
    Create,
    maxLength,
    required,
    SelectInput,
    SimpleForm,
    TextInput 
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { STATUS_CHOICES } from './ProcedureTemplateStatus';


const ProcedureTemplateCreate = props => (
    <Create {...props}>
        <SimpleForm toolbar={<CustomToolbar/>} redirect="list" default>
            <TextInput source="description" validate={[maxLength(50), required()]}/>
            <SelectInput source="statusId" choices={STATUS_CHOICES} defaultValue={1} disabled/>
        </SimpleForm>
    </Create>
);

export default ProcedureTemplateCreate;
