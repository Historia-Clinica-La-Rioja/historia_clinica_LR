import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    number,
    maxLength,
    FormDataConsumer,
    BooleanInput,
    ReferenceInput,
    AutocompleteInput,
    SelectInput,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import UserReferenceInput from "../users/UserReferenceInput";

const InstitutionSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="institutions"
            sort={{ field: 'name', order: 'ASC' }}
            filter={{ institutionId: formData.institutionId }}
        >
            <AutocompleteInput optionText="name" optionValue="id"/>
        </ReferenceInput>);
};

const SnomedGroupSelect = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="snomedgroups"
            sort={{ field: 'description', order: 'ASC' }}
            isRequired={true}
            validate={required()}
        >
            <SelectInput optionText="description" optionValue="id" disabled={true} />
        </ReferenceInput>);
};

const SnomedGroupEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>

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
                number()]}/>

            {/* Parent Snomed Group */}
            <FormDataConsumer>
                {formDataProps => ( <SnomedGroupSelect {...formDataProps} source="groupId" />)}
            </FormDataConsumer>

            {/* Is template */}
            <BooleanInput source="template" disabled={false} initialValue={false}/>

            {/* Institution */}
            <FormDataConsumer>
                {formDataProps => ( <InstitutionSelect {...formDataProps} source="institutionId" />)}
            </FormDataConsumer>

            {/* User */}
            <UserReferenceInput source="userId" />

        </SimpleForm>
    </Edit>
);

export default SnomedGroupEdit;
