import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    ReferenceInput,
    AutocompleteInput,
    FormDataConsumer, usePermissions,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from "../roles";

const InstitutionSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="institutions"
            sort={{ field: 'name', order: 'ASC' }}
            filterToQuery={searchText => ({name: searchText})}
            filter={{ institutionId: formData.institutionId }}
        >
            <AutocompleteInput optionText="name" optionValue="id" resettable />
        </ReferenceInput>);
};

const SnomedGroupSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="snomedgroups"
            filterToQuery={searchText => ({description: "PROCEDURE"})}
            isRequired={true}
        >
            <AutocompleteInput optionText="description" optionValue="id" resettable />
        </ReferenceInput>);
};

const SnomedGroupTypeSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="snomedgrouptypes"
            filterToQuery={searchText => ({id: 3})}
            isRequired={true}
        >
            <AutocompleteInput optionText="description" optionValue="id" resettable />
        </ReferenceInput>);
};

const InstitutionPracticesCreate = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return(
        <Create {...props} hasCreate={userIsAdminInstitutional}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>

                {/* Institution */}
                <FormDataConsumer>
                    {formDataProps => (<InstitutionSelect {...formDataProps} source="institutionId"/>)}
                </FormDataConsumer>

                {/* Description */}
                <TextInput defaultValue={"Prácticas de la institucion"} source="description" validate={[
                    required(),
                    maxLength(100)]}/>

                {/* Custom id */}
                <TextInput defaultValue={"Grupo de prácticas"} source="customId" validate={[
                    required(),
                    maxLength(50)]}
                />

                {/* Parent Snomed Group */}
                <FormDataConsumer>
                    {formDataProps => (<SnomedGroupSelect {...formDataProps} source="groupId"/>)}
                </FormDataConsumer>

                {/* Snomed Group Type */}
                <FormDataConsumer>
                    {formDataProps => (<SnomedGroupTypeSelect {...formDataProps} source="groupType"/>)}
                </FormDataConsumer>

            </SimpleForm>
        </Create>
    );
};

export default InstitutionPracticesCreate;
