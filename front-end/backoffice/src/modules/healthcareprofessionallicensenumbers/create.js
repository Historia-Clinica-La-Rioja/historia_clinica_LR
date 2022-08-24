import React from 'react';
import {
    Create,
    SimpleForm,
    required, ReferenceField, TextField, TextInput, ReferenceInput, FunctionField, SelectInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const redirect = (basePath, id, data) => `/professionalprofessions/${data.professionalProfessionId}/show`;


const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const HealthcareProfessionalsLicenseNumbersCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
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

export default HealthcareProfessionalsLicenseNumbersCreate;
