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
import CustomToolbar from '../../components/CustomToolbar';

const SERVICE = 8;

const searchToFilter = searchText => ({name: searchText ? searchText : -1});

const ServiceField = ({formData, ...rest}) => {
    return formData.typeId !== SERVICE ? null : (
        <ReferenceInput {...rest} sort={{ field: 'name', order: 'ASC' }}
            filterToQuery={searchToFilter}>
            <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
        </ReferenceInput>
    )
}

const HierarchicalUnitCreate = props => {
    const redirect = props?.location?.state?.record?.hierarchicalUnitIdToReport != null ?
        `/hierarchicalunits/${props?.location?.state?.record?.hierarchicalUnitIdToReport}/show` : "show";
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>
                <ReferenceInput
                    source="institutionId"
                    reference="institutions"
                    sort={{field: 'name', order: 'ASC'}}
                >
                    <AutocompleteInput optionText="name" optionValue="id" options={{disabled: true}}/>
                </ReferenceInput>
                <TextInput source="alias" validate={[required()]}/>
                <ReferenceInput
                    reference="hierarchicalunittypes"
                    source="typeId">
                    <SelectInput optionText="description" optionValue="id" validate={[required()]}/>
                </ReferenceInput>
                <FormDataConsumer>
                    {formDataProps => (
                        <ServiceField {...formDataProps} reference="clinicalservices" source="clinicalSpecialtyId"/>)}
                </FormDataConsumer>
                <ReferenceInput
                    source="hierarchicalUnitIdToReport"
                    reference="hierarchicalunits"
                >
                    <AutocompleteInput optionText="alias" optionValue="id" options={{disabled: true}}/>
                </ReferenceInput>
            </SimpleForm>
        </Create>
    )
};

export default HierarchicalUnitCreate;
