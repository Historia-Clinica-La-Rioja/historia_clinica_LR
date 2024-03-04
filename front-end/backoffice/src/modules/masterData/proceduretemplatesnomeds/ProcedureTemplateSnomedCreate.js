import React, { useState, useRef, useEffect } from 'react';
import {
    Create,
    required,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const redirect = (basePath, id, data) => {return `/proceduretemplates/${id}/show`;};

const ProcedureTemplateSnomedCreate = props => {
    const [practice, setPractice] = useState(null);

    //practiceRef lleva el valor de la práctica elegida
    //lo usamos para que funcione la func transform
    //Ver: https://stackoverflow.com/a/73723629
    const practiceRef = useRef(practice);

    //El recurso snomedpractices devuelve
    //un id y un conceptId. Para crear un proceduretemplatesnomed
    //necesitamos mandar:
    // * El conceptId que recibimos de snomedpractices.
    // * El id del proceduretemplate
    //Este transform (más el practiceRef) se usan para armar
    //el request
    const transform = (record) => {
        const pt = practiceRef.current;

        const ret = {
            id: record.id,
            associatedPractices: [{id: pt.conceptId, pt: pt.conceptPt, sctid: pt.conceptId}],
        };
        return ret

    }

    useEffect(() => {
        practiceRef.current = practice;
    }, [practice]);

    return (
    <Create {...props} transform={transform}>
        <SimpleForm toolbar={<CustomToolbar/>} redirect={redirect}>
            <ReferenceInput
                source="snomedId"
                reference="snomedpractices"
                filterToQuery={searchText => ({conceptPt: searchText})}
                fullWidth
            >
                <AutocompleteInput
                    optionText="conceptPt"
                    optionValue="id"
                    validate={[required()]}
                    resettable helperText="* Mínimo 3 caracteres para realizar la búsqueda en Snowstorm"
                    onSelect={(selected) => setPractice(selected)}
                    />
            </ReferenceInput>

        </SimpleForm>
    </Create>)
};

export default ProcedureTemplateSnomedCreate;
