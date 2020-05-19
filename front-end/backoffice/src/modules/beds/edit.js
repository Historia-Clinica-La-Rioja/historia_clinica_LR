import React from 'react';
import {
    Edit,
    TextInput,
    ReferenceInput,
    SelectInput,
    AutocompleteInput,
    SimpleForm,
    required,
    BooleanInput,
} from 'react-admin';

const BedEdit = props => (
    <Edit {...props}>
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
            <BooleanInput source="enabled" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="available" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="free" validate={[required()]} disabled={false} initialValue={true}/>
        </SimpleForm>
    </Edit>
);

export default BedEdit;
