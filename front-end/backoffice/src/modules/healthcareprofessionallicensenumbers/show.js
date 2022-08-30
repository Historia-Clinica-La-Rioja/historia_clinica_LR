import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField, FunctionField,
} from 'react-admin';


const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const HealthcareProfessionalsLicenseNumbersShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField
                label="resources.healthcareprofessionallicensenumbers.fields.personId"
                source="professionalProfessionId" reference="professionalprofessions" link={false}>
                <ReferenceField source="personId" reference="person">
                    <FunctionField render={renderPerson}/>
                </ReferenceField>
            </ReferenceField>
            <ReferenceField
                label="resources.healthcareprofessionallicensenumbers.fields.professionalSpecialtyId"
                source="professionalProfessionId" reference="professionalprofessions" link={false}>
                <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" link={false}>
                    <TextField source="description" />
                </ReferenceField>
            </ReferenceField>
            <TextField source="licenseNumber" />
            <ReferenceField source="typeId" reference="licensenumbertypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalsLicenseNumbersShow;
