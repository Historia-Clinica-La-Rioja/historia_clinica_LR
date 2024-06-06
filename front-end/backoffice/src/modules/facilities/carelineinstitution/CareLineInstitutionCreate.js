import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    FormDataConsumer,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const CareLineSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="carelines"
            sort={{ field: 'description', order: 'ASC' }}
            filterToQuery={searchText => ({description: searchText})}
        >
            <AutocompleteInput optionText="description" optionValue="id" resettable isRequired={true} />
        </ReferenceInput>);
};

const InstitutionSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="institutions"
            sort={{ field: 'name', order: 'ASC' }}
            filterToQuery={searchText => ({name: searchText})}
            filter={{ institutionId: formData.institutionId }}
        >
            <AutocompleteInput optionText="name" optionValue="id" resettable isRequired={true} />
        </ReferenceInput>);
};

const CareLineInstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>

            <FormDataConsumer>
                {formDataProps => ( <InstitutionSelect {...formDataProps} source="institutionId" />)}
            </FormDataConsumer>

            <FormDataConsumer>
                {formDataProps => ( <CareLineSelect {...formDataProps} source="careLineId" />)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default CareLineInstitutionCreate;
