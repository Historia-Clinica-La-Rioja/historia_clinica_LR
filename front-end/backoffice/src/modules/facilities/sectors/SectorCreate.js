import React from 'react';
import { useForm } from 'react-final-form';
import {
    AutocompleteInput,
    Create,
    FormDataConsumer,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextInput,
    BooleanInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const INTERNACION = 2;
const SIN_TIPO = 6;
const DIAGNOSTICO_POR_IMAGENES = 4;

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show`;

const SectorType = (sourceId) => {
    const form = useForm();

    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            sort={{ field: 'description', order: 'ASC' }}
            defaultValue={SIN_TIPO}
            onChange={ _ => {
                form.change('informer', false);
                form.change('ageGroupId', null);
                form.change('sectorOrganizationId', null);
                form.change('careTypeId', null);
                form.change('hospitalizationTypeId', null);
            }
        }
        >
            <SelectInput optionText="description" optionValue="id"/>
        </ReferenceInput>);

};

const HospitalizationField = ({formData, ...rest}) => {
    return formData.sectorTypeId !== INTERNACION ? null : (
        <ReferenceInput {...rest} sort={{ field: 'description', order: 'ASC' }}>
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>
    )
}

const CheckBoxField = ({formData}) => {

    return formData.sectorTypeId !== DIAGNOSTICO_POR_IMAGENES ? null : (
        <BooleanInput label="resources.sectors.fields.informer" source="informer" />
    )
}

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
            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>

            {/*Sector Type*/}
            <SectorType source="sectorTypeId"/>
            
            {/*Informer Sector*/}
            <FormDataConsumer>
                {formDataProps => (<CheckBoxField{... formDataProps} />)}
            </FormDataConsumer>

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
