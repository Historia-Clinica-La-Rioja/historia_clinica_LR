import React from 'react';
import {
    Create, 
    SimpleForm, 
    TextInput, 
    BooleanInput
} from 'react-admin';
import { CustomToolbar } from '../../components';

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show/1`;

const InstitutionMedicineGroupCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>
            <TextInput source="name"/>
            <span>Cobertura pública exclusiva</span>
            <br/>
            <BooleanInput source="requiresAudit" initialValue={false}/>
            <span>Ámbito</span>
            <br/>
            <BooleanInput source="outpatient" initialValue={false}/>
            <BooleanInput source="emergencyCare" initialValue={false}/>
            <BooleanInput source="internment" initialValue={false}/>
            <span>Diagnósticos y problemas</span>
            <br/>
            <BooleanInput source="allDiagnoses" initialValue={true}/>
            <span>Mensaje para indicaciones</span>
            <br/>
            <TextInput source="message" label="Ejemplo de indicaciones"/>
        </SimpleForm>
    </Create>
);

export default InstitutionMedicineGroupCreate;