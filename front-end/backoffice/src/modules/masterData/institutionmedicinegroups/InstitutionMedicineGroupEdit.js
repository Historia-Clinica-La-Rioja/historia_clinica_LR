import React from 'react';
import {
    Edit, 
    SimpleForm, 
    TextInput, 
    BooleanInput,
    useGetOne,
} from 'react-admin';
import { CustomToolbar } from '../../components';

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show/1`;

const InstitutionMedicineGroupEdit = (props) => {
    const group = useGetOne('institutionmedicinegroups', props.id);
    let isDomain = group.data?.isDomain;
    return (
    <Edit {...props} hasShow={true} >
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar isEdit={!isDomain}/>}>
            <TextInput source="name" disabled={isDomain} /> 
            <span>Cobertura pública exclusiva</span>
            <br/>
            <BooleanInput source="requiresAudit" disabled={isDomain}/>
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