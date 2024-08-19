import React from 'react';
import { 
    SimpleForm, 
    Create, 
    TextInput, 
    BooleanInput 
} from 'react-admin';
import { CustomToolbar } from '../../components';


const MedicineGroupCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>
            <TextInput source="name"/>
            <span>Cobertura pública exclusiva</span>
            <br/>
            <BooleanInput source="requiresAudit" initialValue={false}/>
            <span>Ámbito</span>
            <br/>
            <BooleanInput source="outpatient" initialValue={false}/>
            <BooleanInput source="emergencyCare" initialValue={false}/>
            <BooleanInput source="internment" initialValue={false}/>
            <span>Mensaje para indicaciones</span>
            <br/>
            <TextInput source="message" label="Ejemplo de indicaciones"/>
        </SimpleForm>
    </Create>
);

export default MedicineGroupCreate;