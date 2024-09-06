import React from 'react';
import {
    Edit, 
    SimpleForm, 
    TextInput, 
    BooleanInput,
    useGetOne,
} from 'react-admin';
import { CustomToolbar } from '../../components';
import { useState } from 'react';

const goBack = () => {
    window.history.back();
}

const InstitutionMedicineGroupEdit = (props) => {

    const [record, setRecord] = useState({});

    const [isDomain, setIsDomain] = useState(false);
    
    const [requiresAudit, setRequiresAudit] = useState(record?.requiresAudit);
    
    useGetOne('institutionmedicinegroups', props.id, {
        onSuccess: (data) => {
            setRecord(data);
            setIsDomain(data.data?.isDomain);
            setRequiresAudit(data.data?.requiresAudit);
        },
    });

    const handleRequiresAuditChange = (event) => {
        setRequiresAudit(event);
    };
    
    return (
    <Edit {...props} hasShow={true} >
        <SimpleForm redirect={goBack} toolbar={<CustomToolbar isEdit={!isDomain}/>}>
            <TextInput source="name" disabled={isDomain}/> 
            <span>Cobertura pública exclusiva</span>
            <br/>
            <BooleanInput source="requiresAudit" disabled={isDomain} onChange={handleRequiresAuditChange}/>
            {requiresAudit && <TextInput fullWidth multiline source="requiredDocumentation" disabled={isDomain || !requiresAudit}/>}
            <span>Ámbito</span>
            <br/>
            <BooleanInput source="outpatient" disabled={isDomain}/>
            <BooleanInput source="emergencyCare" disabled={isDomain}/>
            <BooleanInput source="internment" disabled={isDomain}/>
            <span>Diagnósticos y problemas</span>
            <br/>
            <BooleanInput label="Incluir todos" source="allDiagnoses" disabled={isDomain} />
            <span>Mensaje para indicaciones</span>
            <br/>
            <TextInput source="message" label="" disabled={isDomain} />      
            {isDomain && <BooleanInput source="enabled" /> }
        </SimpleForm>
    </Edit>
    );
}

export default InstitutionMedicineGroupEdit;