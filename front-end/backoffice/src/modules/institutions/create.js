import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    ReferenceInput,
    SelectInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const Dependency = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="dependencies"
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" validate={[required()]} />
        </ReferenceInput>);

};

const InstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="website" />
            <TextInput source="phone" validate={[required()]} />
            <TextInput source="email" type="email" validate={[required()]} />
            <TextInput source="cuit" validate={[required()]} />
            <TextInput source="sisaCode" validate={[required()]} />
            <Dependency source="dependencyId" />
        </SimpleForm>
    </Create>
);

export default InstitutionCreate;
