import React, { useState } from 'react';
import {
    BooleanInput,
    Datagrid,
    Edit,
    Pagination,
    ReferenceManyField,
    required,
    SimpleForm,
    TextInput
} from 'react-admin';
import {
    SgxDateField,
    SgxSelectInput,
    CustomToolbar,
} from '../../components';

const BedEdit = props => {
    const [enabled, setEnabled] = useState(true);
    const [available, setAvailable] = useState(true);
    const [free, setFree] = useState(true);
    const [canSave, setCanSave] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    const handleEnabledChange = (value) => {
        setEnabled(value);
        validateFields(value, available, free);
    };

    const handleAvailableChange = (value) => {
        setAvailable(value);
        validateFields(enabled, value, free);
    };

    const handleFreeChange = (value) => {
        setFree(value);
        validateFields(enabled, available, value);
    };

    const validateFields = (enabledValue, availableValue, freeValue) => {
        if (!enabledValue && availableValue) {
            setCanSave(false);
            setErrorMessage('No se puede guardar un cama no habilitada y disponible.');
        } else if (!availableValue && freeValue) {
            setCanSave(false);
            setErrorMessage('No se puede guardar una cama no disponible y libre.');
        } else {
            setCanSave(true);
            setErrorMessage('');
        }
    };

    return (
        <Edit {...props}>
            <SimpleForm
                redirect="show"
                toolbar={<CustomToolbar isEdit={true} canSave={canSave} />}
            >
                <TextInput source="bedNumber" validate={[required()]} disabled={!canSave} />

                <SgxSelectInput source="roomId" element="rooms" optionText="description" alwaysOn allowEmpty={false} disabled={!canSave} />

                <BooleanInput 
                    source="enabled" 
                    validate={[required()]} 
                    onChange={handleEnabledChange} 
                    disabled={false} 
                    initialValue={true}
                />
                <BooleanInput 
                    source="available" 
                    validate={[required()]} 
                    onChange={handleAvailableChange} 
                    disabled={false} 
                    initialValue={true}
                />
                <BooleanInput 
                    source="free" 
                    onChange={handleFreeChange} 
                    disabled={false} 
                    initialValue={true}
                />

                <ReferenceManyField
                    addLabel={true}
                    label="resources.beds.fields.episodes"
                    reference="internmentepisodes"
                    target="bedId"
                    sort={{ field: 'entryDate', order: 'DESC' }}
                    filter={{ statusId: 1 }}
                    pagination={<Pagination />}
                >
                    <Datagrid>
                        <SgxDateField source="entryDate" />
                    </Datagrid>
                </ReferenceManyField>

                {!canSave && (
                    <div style={{ color: 'red', marginTop: '10px' }}>
                        {errorMessage}
                    </div>
                )}
            </SimpleForm>
        </Edit>
    );
};

export default BedEdit;
