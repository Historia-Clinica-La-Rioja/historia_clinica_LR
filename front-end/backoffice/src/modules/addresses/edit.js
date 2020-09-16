import React, { Fragment } from 'react';
import {
    TextInput,
    ReferenceInput,
    SelectInput,
    Edit,
    SimpleForm,
    FormDataConsumer,
    required,
    NumberInput
} from 'react-admin';

import { useForm } from 'react-final-form';

const Province = (sourceId) => {
    const form = useForm();

    return (
    <ReferenceInput
        {...sourceId}
        reference="provinces" 
        perPage={100} //Así traemos todas las provincias de una
        sort={{ field: 'description', order: 'ASC' }}
        onChange={value => {
            form.change('departmentId', null);
            form.change('cityId', null);
        }}
    >
        <SelectInput optionText="description" optionValue="id" validate={[required()]} />
    </ReferenceInput>);

};

const Department = ({ formData, ...rest }) => {
    const form = useForm();
    // Wait for the province to be selected
    if (!formData.provinceId) return null;

    return (
        <Fragment>
            <ReferenceInput
                {...rest}
                reference="departments"
                filter={{ provinceId: formData ? formData.provinceId : '' }}
                sort={{ field: 'description', order: 'ASC' }}
                perPage={1000} //Así traemos todos los departamentos
                onChange={value => form.change('cityId', null)}
            >
                <SelectInput optionText="description" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </Fragment>
    );
};


const City = ({ formData, ...rest }) => {
    if (!formData.departmentId) return null;
    return <ReferenceInput
        {...rest}
        reference="cities"
        filter={{ departmentId: formData.departmentId }}
        sort={{ field: 'description', order: 'ASC' }}
        perPage={1000} //Así traemos todos los departamentos
    >
        <SelectInput optionText="description" optionValue="id" validate={[required()]} />
    </ReferenceInput>
};
const AddressEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >

            {/*Province*/}
            <Province source="provinceId"/>
            {/*Department*/}
            <FormDataConsumer>
                {formDataProps => ( <Department {...formDataProps} source="departmentId"/>)}
            </FormDataConsumer>
            {/*City*/}
            <FormDataConsumer>
                {formDataProps => ( <City {...formDataProps} source="cityId"/>)}
            </FormDataConsumer>
            <TextInput source="street" validate={[required()]} />
            <TextInput source="number" validate={[required()]} />
            <TextInput source="floor" />
            <TextInput source="apartment" />
            <TextInput source="quarter" />
            <TextInput source="postcode" validate={[required()]} />
            <NumberInput source="latitude" />
            <NumberInput source="longitude" />

        </SimpleForm>
    </Edit>
);

export default AddressEdit;
