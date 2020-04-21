import React from 'react';
import {
    Create,
    TextInput,
    ReferenceInput,
    SelectInput,
    AutocompleteInput,
    SimpleForm,
    required
} from 'react-admin';

const BedCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="bedNumber" validate={[required()]} />
            <ReferenceInput
                source="roomId"
                reference="rooms"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <AutocompleteInput optionText="description" optionValue="id"/>
            </ReferenceInput>
            <ReferenceInput
                source="bedCategoryId"
                reference="bedcategories"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default BedCreate;
