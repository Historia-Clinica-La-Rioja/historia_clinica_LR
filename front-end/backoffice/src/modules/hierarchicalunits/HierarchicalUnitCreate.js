import React from 'react';
import {
    Create,
    TextInput,
    SimpleForm,
    required,
    ReferenceInput,
    SelectInput,
    AutocompleteInput,
    FormDataConsumer,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const SERVICE = 8;

const searchToFilter = searchText => ({name: searchText ? searchText : -1});

const ServiceField = ({formData, ...rest}) => {
    return formData.typeId !== SERVICE ? null : (
        <ReferenceInput {...rest}
            filterToQuery={searchToFilter}>
            <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
        </ReferenceInput>
    )
}

const HierarchicalUnitCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
            >
                <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>
            <TextInput source="alias" validate={[required()]} />
            <ReferenceInput
                reference="hierarchicalunittypes"
                source="typeId">
                <SelectInput optionText="description" optionValue="id" validate={[required()]}/>
            </ReferenceInput>
            <FormDataConsumer>
                {formDataProps => ( <ServiceField {...formDataProps} reference="clinicalservices" source="clinicalSpecialtyId"/>)}
            </FormDataConsumer>
        </SimpleForm>
    </Create>
);

export default HierarchicalUnitCreate;
