import React from 'react';
import {
    AutocompleteInput,
    Edit,
    FormDataConsumer,
    ReferenceInput,
    required, SelectInput,
    SimpleForm,
    TextInput,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const SERVICE = 8;

const ServiceField = ({formData, ...rest}) => {
    return formData.typeId !== SERVICE ? null : (
        <ReferenceInput {...rest} sort={{ field: 'description', order: 'ASC' }}>
            <SelectInput optionText="name" optionValue="id" />
        </ReferenceInput>
    )
}
const HierarchicalUnitEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
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
    </Edit>
);

export default HierarchicalUnitEdit;
