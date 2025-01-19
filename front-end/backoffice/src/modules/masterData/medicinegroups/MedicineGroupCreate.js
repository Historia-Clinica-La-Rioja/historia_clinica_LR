import React from 'react';
import { 
    SimpleForm, 
    Create, 
    TextInput, 
    BooleanInput
} from 'react-admin';
import { CustomToolbar } from '../../components';
import { useState } from 'react';


const MedicineGroupCreate = props => {

    const [requiresAudit, setRequiresAudit] = useState(false);

    const handleRequiresAuditChange = (event) => {
        setRequiresAudit(event);
    };

    return (
        <Create {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>
                <TextInput source="name"/>
                <span>Cobertura pública exclusiva</span>
                <br/>
                <BooleanInput source="requiresAudit" initialValue={false} onChange={handleRequiresAuditChange}/>
                {requiresAudit && <TextInput fullWidth multiline source="requiredDocumentation"/>}
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
};

export default MedicineGroupCreate;