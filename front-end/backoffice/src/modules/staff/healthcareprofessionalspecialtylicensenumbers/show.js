import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField, FunctionField,
} from 'react-admin';


const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const HealthcareProfessionalSpecialtyLicenseNumbersShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField
                label="resources.healthcareprofessionalspecialtylicensenumbers.fields.personId"
                source="healthcareProfessionalSpecialtyId" reference="healthcareprofessionalspecialties" link={false}>
                <ReferenceField
                    source="professionalProfessionId" reference="professionalprofessions" link={false}>
                    <ReferenceField source="personId" reference="person">
                        <FunctionField render={renderPerson}/>
                    </ReferenceField>
                </ReferenceField>
            </ReferenceField>
            <ReferenceField
                label="resources.healthcareprofessionalspecialtylicensenumbers.fields.professionalSpecialtyId"
                source="healthcareProfessionalSpecialtyId" reference="healthcareprofessionalspecialties" link={false}>
                <ReferenceField
                    source="professionalProfessionId" reference="professionalprofessions" link={false}>
                    <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" link={false}>
                        <TextField source="description" />
                    </ReferenceField>
                </ReferenceField>
            </ReferenceField>
            <ReferenceField
                label="resources.healthcareprofessionalspecialtylicensenumbers.fields.clinicalSpecialtyId"
                source="healthcareProfessionalSpecialtyId" reference="healthcareprofessionalspecialties" link={false}>
                <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties" link={false}>
                    <TextField source="name" />
                </ReferenceField>
            </ReferenceField>
            <TextField source="licenseNumber" />
            <ReferenceField source="typeId" reference="licensenumbertypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalSpecialtyLicenseNumbersShow;
