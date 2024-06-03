import React from 'react';
import {
    Edit, 
    SimpleForm, 
    TextInput, 
    BooleanInput
} from 'react-admin';
import { CustomToolbar } from '../../components';

const MedicineGroupEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="name"/>
            <span>Cobertura pública exclusiva</span>
            <br/>
            <BooleanInput source="requiresAudit"/>
            <span>Ámbito</span>
            <br/>
            <BooleanInput source="outpatient"/>
            <BooleanInput source="emergencyCare"/>
            <BooleanInput source="internment"/>
            <span>Mensaje para indicaciones</span>
            <br/>
            <TextInput source="message" label=""/>        
        </SimpleForm>
    </Edit>
)

export default MedicineGroupEdit;