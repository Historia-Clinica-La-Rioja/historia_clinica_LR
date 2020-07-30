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

const Province = () => {
    const form = useForm();

    return (
    <ReferenceInput 
        source="provinceId"
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
                source="departmentId"
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
        source="cityId"
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
            <Province />
            {/*Department*/}
            <FormDataConsumer>
                {formDataProps => ( <Department {...formDataProps} />)}
            </FormDataConsumer>
            {/*City*/}
            <FormDataConsumer>
                {formDataProps => ( <City {...formDataProps} />)}
            </FormDataConsumer>
            <TextInput source="street" validate={[required()]} />
            <TextInput source="number" validate={[required()]} />
            <TextInput source="floor" />
            <TextInput source="apartment" />
            <TextInput source="quarter" />
            <TextInput source="postcode" validate={[required()]} />
            <NumberInput source="latitud" />
            <NumberInput source="longitud" />

        </SimpleForm>
    </Edit>
);

export default AddressEdit;
