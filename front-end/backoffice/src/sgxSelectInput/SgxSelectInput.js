import React from 'react';
import {useState, useEffect} from 'react';

import {
    SelectInput,
 } from 'react-admin';

 import { sgxFetchApiWithToken } from '../libs/sgx/api/fetch';

const backofficeUrl = 'backoffice';
const SgxSelectInput = ({ element, ...props})  =>{   
    const [elements, setElements] = useState([]);
    useEffect(() => {
        const url = `${backofficeUrl}/${element}/elements`;
        sgxFetchApiWithToken(url)
            .then(json => {
                setElements(json);
            }, ({ status }) => {
                console.error(status);
            }
        );
    }, [element]);
    return elements.length ? <SelectInput choices={elements} {...props}/> : <span />;
};


export default SgxSelectInput;
