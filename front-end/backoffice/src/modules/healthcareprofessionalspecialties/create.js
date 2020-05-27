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

import PeopleReferenceInput from '../people/PeopleReferenceInput';

const Person = () => {
    const form = useForm();
    return (
        <PeopleReferenceInput
            source="personId"
            onChange={value => { form.change('healthcareProfessionalId', null); }}
            label="Persona"
        />
    );
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
const searchToFilter = searchText => ({description: searchText ? searchText : -1});
const renderSpecialty = (choice) => `${choice.description} - ${choice.descriptionProfessionRef}`;
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
                filterToQuery={searchToFilter}
            >
                <AutocompleteInput optionText={renderSpecialty} optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalSpecialtyCreate;
