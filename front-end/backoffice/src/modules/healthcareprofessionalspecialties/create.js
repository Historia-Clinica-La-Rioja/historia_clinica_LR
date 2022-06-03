import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required, ReferenceField, FunctionField, TextField
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const searchSpecialtyToFilter = searchText => ({name: searchText ? searchText : -1});
const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
const redirect = (basePath, id, data) => `/professionalprofessions/${data.professionalProfessionId}/show`;

const HealthcareProfessionalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <ReferenceField label="resources.healthcareprofessionalspecialties.fields.personId"
                source="professionalProfessionId" reference="professionalprofessions" link={false}>
                <ReferenceField source="personId" reference="person">
                    <FunctionField render={renderPerson}/>
                </ReferenceField>
            </ReferenceField>
            <ReferenceField label="resources.healthcareprofessionalspecialties.fields.professionalSpecialtyId"
                source="professionalProfessionId" reference="professionalprofessions" link={false}>
                <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" link={false}>
                    <TextField source="description" />
                </ReferenceField>
            </ReferenceField>

            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalspecialties"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchSpecialtyToFilter}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalSpecialtyCreate;
