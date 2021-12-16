import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    ReferenceInput,
    SelectInput,
    number,
    maxLength,
    minLength
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

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
            <TextInput source="phone" validate={[
                required(),
                maxLength(20)]}/>
            <TextInput source="email" type="email" validate={[required()]} />
            <TextInput source="cuit" validate={[
                required(),
                number(),
                maxLength(20)]}/>
            <TextInput source="sisaCode" validate={[
                required(),
                number(),
                minLength(14),
                maxLength(14)]}/>
            <Dependency source="dependencyId" />
            <TextInput source="provinceCode"/>
        </SimpleForm>
    </Create>
);

export default InstitutionCreate;
