import { React, useState } from 'react';
import {
    Edit, 
    SimpleForm, 
    TextInput, 
    BooleanInput,
    useGetOne
} from 'react-admin';
import { CustomToolbar } from '../../components';

const MedicineGroupEdit = props => {

    const { data: record } = useGetOne('medicinegroups', props.id);

    const [requiresAudit, setRequiresAudit] = useState(record?.requiresAudit);

    const handleRequiresAuditChange = (event) => {
        setRequiresAudit(event);
    };

    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
                <TextInput source="name"/>
                <span>Cobertura pública exclusiva</span>
                <br/>
                <BooleanInput source="requiresAudit" onChange={handleRequiresAuditChange}/>
                {requiresAudit && <TextInput fullWidth multiline source="requiredDocumentation"/>}
                <span>Ámbito</span>
                <br/>
                <BooleanInput source="outpatient"/>
                <BooleanInput source="emergencyCare"/>
                <BooleanInput source="internment"/>
                <span>Diagnósticos y problemas</span>
                <br/>
                <BooleanInput source="allDiagnoses" />
                <span>Mensaje para indicaciones</span>
                <br/>
                <TextInput fullWidth multine source="message" label=""/>        
            </SimpleForm>
        </Edit>
    );
}

export default MedicineGroupEdit;