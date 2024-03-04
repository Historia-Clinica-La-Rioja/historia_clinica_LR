import React from "react";
import {
    DateInput,
} from 'react-admin';

const SgxDateInput = ({ source, ...props})  => {
    return <DateInput source={source} {...props} />
}

SgxDateInput.defaultProps = {
    locales: "es-AR",
    addLabel: true
}

export default SgxDateInput;