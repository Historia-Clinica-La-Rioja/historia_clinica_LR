import React from 'react';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';
import {DeleteButton, Edit, SimpleForm, TextInput} from 'react-admin';

const ClinicalSpecialtyMandatoryMedicalPracticeEdit = props => (

    <Edit {...props} >
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
    </Edit>
);

export default ClinicalSpecialtyMandatoryMedicalPracticeEdit;