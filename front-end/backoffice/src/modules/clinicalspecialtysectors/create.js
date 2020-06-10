import React, { Fragment } from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    FormDataConsumer,
    required
} from 'react-admin';
import { useForm } from 'react-final-form';

const InstitutionInput = () => {
    const form = useForm();
    return (
    <ReferenceInput
        source="institutionId"
        reference="institutions"
        sort={{ field: 'name', order: 'ASC' }}
        onChange={value => {
            form.change('sectorId', null);
        }}
        filterToQuery={searchText => ({name: searchText})}                
    >
        <AutocompleteInput optionText="name" optionValue="id"/>
    </ReferenceInput>
    );
};

const SectorInput = ({ formData, ...rest }) => {
    // Wait for the institution to be selected
    if (!formData.institutionId) return null;

    return (
        <Fragment>

            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
                filter={{ institutionId: formData ? formData.institutionId : -1 }}
                label="resources.clinicalspecialtysectors.fields.sectorId"
            >
                <AutocompleteInput optionText="description" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </Fragment>
    );
};

const searchToFilter = searchText => ({name: searchText ? searchText : -1});
const ClinicalSpecialtySectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            {/*Este input nos permite filtrar los sectores por institucion*/}
            <InstitutionInput />
            <FormDataConsumer>
                {formDataProps => ( <SectorInput {...formDataProps} />)}
            </FormDataConsumer>
            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalspecialties"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchToFilter}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtySectorCreate;
