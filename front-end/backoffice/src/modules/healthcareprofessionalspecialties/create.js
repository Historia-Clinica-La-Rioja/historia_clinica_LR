import React, { Fragment } from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    FormDataConsumer,
    SelectInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import { useForm } from 'react-final-form';

import renderPerson from '../components/renderperson'

const Person = () => {
    const form = useForm();
    return (
    <ReferenceInput
        source="personId"
        label="resources.healthcareprofessionalspecialties.fields.personId"
        reference="people"
        sort={{ field: 'identificationNumber', order: 'ASC' }}
        filterToQuery={searchText => ( searchText ? { identificationNumber: searchText } : '')}
        validate={[required()]}
        onChange={value => {
            form.change('healthcareProfessionalId', null);
        }}
    >
        <AutocompleteInput optionText={renderPerson} optionValue="id"/>
    </ReferenceInput>
    )
};

const HealthcareProfessionalInput = ({ formData, ...rest }) => {
    // Wait for the province to be selected
    if (!formData.personId) return null;

    return (
        <Fragment>
            <ReferenceInput
                source="healthcareProfessionalId"
                reference="healthcareprofessionals"
                label="resources.healthcareprofessionalspecialties.fields.healthcareProfessionalId"
                filter={{ personId: formData ? formData.personId : '' }}
                sort={{ field: 'licenseNumber', order: 'ASC' }}
            >
                <SelectInput optionText="licenseNumber" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </Fragment>
    );
};

const HealthcareProfessionalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <Person />
            <FormDataConsumer>
                {formDataProps => ( <HealthcareProfessionalInput {...formDataProps} />)}
            </FormDataConsumer>
            <ReferenceInput
                source="professionalSpecialtyId"
                reference="professionalspecialties"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <AutocompleteInput optionText="description" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalSpecialtyCreate;
