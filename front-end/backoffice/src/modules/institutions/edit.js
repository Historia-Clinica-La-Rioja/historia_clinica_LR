import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    ReferenceManyField,
    Datagrid,
    TextField,
    EditButton,
    ReferenceInput, 
    SelectInput,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
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

const InstitutionEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="website" />
            <TextInput source="phone" validate={[required()]} />
            <TextInput source="email" type="email" validate={[required()]} />
            <TextInput source="cuit" validate={[required()]} />
            <TextInput source="sisaCode" validate={[required()]} />
            <Dependency source="dependencyId" />

            <SectionTitle label="resources.institutions.fields.sectors"/>
            <CreateRelatedButton
                reference="sectors"
                refFieldName="institutionId"
                label="resources.sectors.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="sectors"
                target="institutionId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default InstitutionEdit;
