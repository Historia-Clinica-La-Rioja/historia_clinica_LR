import React from 'react';
import {
    Create,
    SimpleForm,
    required, ReferenceField, TextField, TextInput, ReferenceInput, FunctionField, SelectInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const redirect = (basePath, id, data) => `/healthcareprofessionalspecialties/${data.healthcareProfessionalSpecialtyId}/show`;


const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const HealthcareProfessionalSpecialtyLicenseNumbersCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
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
            <TextInput source="licenseNumber" validate={[required()]} />
            <ReferenceInput
                source="typeId"
                reference="licensenumbertypes"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalSpecialtyLicenseNumbersCreate;
