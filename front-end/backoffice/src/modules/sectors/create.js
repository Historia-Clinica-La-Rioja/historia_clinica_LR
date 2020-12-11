import React from 'react';
import {
    AutocompleteInput,
    Create,
    FormDataConsumer,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const INTERNACION = 2;

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show`;

const SectorType = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);

};

const HospitalizationField = ({formData, ...rest}) => {
    return formData.sectorTypeId !== INTERNACION ? null : (
        <ReferenceInput {...rest} sort={{ field: 'description', order: 'ASC' }}>
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>
    )
}

const Sector = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="sectors"
            sort={{ field: 'description', order: 'ASC' }}
            filter={{institutionId: formData.institutionId}}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const SectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />} >
            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
            >
                <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>

            {/*Parent Sector*/}
            <FormDataConsumer>
                {formDataProps => ( <Sector {...formDataProps} source="sectorId" />)}
            </FormDataConsumer>

            {/*Sector Type*/}
            <SectorType source="sectorTypeId"/>

            {/*Age Groups*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="agegroups" source="ageGroupId"/>)}
            </FormDataConsumer>
            {/*Sector Organizations*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="sectororganizations" source="sectorOrganizationId"/>)}
            </FormDataConsumer>
            {/*Care Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="caretypes" source="careTypeId" />)}
            </FormDataConsumer>
            {/*Hospitalization Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="hospitalizationtypes" source="hospitalizationTypeId"/>)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default SectorCreate;
