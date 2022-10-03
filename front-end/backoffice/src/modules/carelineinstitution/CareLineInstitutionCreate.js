import React from 'react';
import {
    Create,
    SimpleForm,
    required,
    usePermissions, ReferenceInput, AutocompleteInput, FormDataConsumer,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import {ADMINISTRADOR} from "../roles";

const CareLineSelect = ({ formData, ...rest }) => {
    const { permissions } = usePermissions();
    const userIsAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role)).length > 0;
    return (
        <ReferenceInput
            {...rest}
            reference="carelines"
            sort={{ field: 'description', order: 'ASC' }}
            filterToQuery={searchText => ({description: searchText})}
            validate={!userIsAdmin ? [required()] : []}
        >
            <AutocompleteInput optionText="description" optionValue="id" resettable />
        </ReferenceInput>);
};

const InstitutionSelect = ({ formData, ...rest }) => {
    const { permissions } = usePermissions();
    const userIsAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role)).length > 0;
    return (
        <ReferenceInput
            {...rest}
            reference="institutions"
            sort={{ field: 'name', order: 'ASC' }}
            filterToQuery={searchText => ({name: searchText})}
            filter={{ institutionId: formData.institutionId }}
            validate={!userIsAdmin ? [required()] : []}
        >
            <AutocompleteInput optionText="name" optionValue="id" resettable />
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
