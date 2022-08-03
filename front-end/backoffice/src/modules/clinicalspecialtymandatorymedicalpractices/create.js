import React from 'react';
import {Create, DeleteButton, SimpleForm, TextInput} from 'react-admin';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const ClinicalSpecialtyMandatoryMedicalPracticeCreate = props => (

    <Create {...props} >
        <SimpleForm redirect="list">
            <SgxSelectInput source="clinicalSpecialtyId"
                    element="clinicalspecialties"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false} />
            <SgxSelectInput source="mandatoryMedicalPracticeId"
                    element="mandatorymedicalpractices"
                    optionText="description"
                    alwaysOn
                    allowEmpty={false} />
            <TextInput source="practiceRecommendations" />
            <DeleteButton />
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtyMandatoryMedicalPracticeCreate;