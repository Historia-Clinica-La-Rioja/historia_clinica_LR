import React from 'react';
import {useState, useEffect} from 'react';

import {
    SelectInput
 } from 'react-admin';

import apiRest from '../providers/utils/sgxApiRest';

const backofficeUrl = '/backoffice';
const SgxSelectInput = ({ element, ...props})  =>{   
    const [elements, setElements] = useState([]);
    useEffect(() => {
        const url = `${backofficeUrl}/${element}/elements`;
        apiRest.fetch(url)
            .then(response => {
                const { json } = response;                 
                setElements(json);
            }, ({ status, statusText, body }) => {
                console.error(status);
            }
        );
    }, [element]);
    return <SelectInput choices={elements} {...props}/>;
};


export default SgxSelectInput;
