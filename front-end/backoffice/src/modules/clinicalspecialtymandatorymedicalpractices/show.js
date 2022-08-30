import React from 'react';
import {
    ReferenceField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const ClinicalSpecialtyMandatoryMedicalPracticeShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name"/>
            </ReferenceField>
            <ReferenceField source="mandatoryMedicalPracticeId" reference="mandatorymedicalpractices">
                <TextField source="description"/>
            </ReferenceField>
                <TextField source="practiceRecommendations" />
        </SimpleShowLayout>
    </Show>
);

export default ClinicalSpecialtyMandatoryMedicalPracticeShow;