import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    maxLength,
    FormDataConsumer,
    ReferenceInput,
    AutocompleteInput,
    usePermissions, Toolbar, DeleteButton,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ROOT} from "../roles";
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";

const searchSnomedGroupToFilter = searchText => ({description: searchText ? searchText : ''});

const UserDeleteToolbar = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <Toolbar {...props}>
            <CustomToolbar/>
            <DeleteButton disabled={userIsRootOrAdmin} />
        </Toolbar>
    )
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
            <AutocompleteInput disabled={true} optionText="name" optionValue="id" resettable />
        </ReferenceInput>);
};

const SnomedGroupSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="snomedgroups"
            sort={{ field: 'description', order: 'ASC' }}
            filterToQuery={searchSnomedGroupToFilter}
            isRequired={true}
            validate={required()}
        >
            <AutocompleteInput optionText="description" optionValue="id" disabled={true} />
        </ReferenceInput>);
};

const SnomedGroupEdit = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsAdminInstitutional}>
        <SimpleForm redirect="show" toolbar={<UserDeleteToolbar />}>

            {/* Description */}
            <TextInput source="description" validate={[
                required(),
                maxLength(100)]}/>

            {/* ECL */}
            <TextInput source="ecl"
                       disabled={true}
                       validate={[
                           required(),
                           maxLength(255)]}/>

            {/* Custom id */}
            <TextInput source="customId" validate={[
                required(),
                maxLength(50)]}
            />

            {/* Parent Snomed Group */}
            <FormDataConsumer>
                {formDataProps => (<SnomedGroupSelect {...formDataProps} source="groupId"/>)}
            </FormDataConsumer>

            {/* Snomed Group Type */}
            <FormDataConsumer>
                {formDataProps => ( <SgxSelectInput disabled={true} {...formDataProps} source="groupType" element="snomedgrouptypes" optionText="description" allowEmpty={false} />)}
            </FormDataConsumer>

            {/* Institution */}
            <FormDataConsumer>
                {formDataProps => (<InstitutionSelect {...formDataProps} source="institutionId"/>)}
            </FormDataConsumer>

        </SimpleForm>
    </Edit>)
};

export default SnomedGroupEdit;
