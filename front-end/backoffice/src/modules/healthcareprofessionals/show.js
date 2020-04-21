import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    BooleanField,
    FunctionField
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
const HealthcareProfessionalShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="licenseNumber" />
            <ReferenceField source="personId" reference="people">
                <FunctionField render={renderPerson}/>
            </ReferenceField>
            <BooleanField source="isMedicalDoctor" />
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalShow;
