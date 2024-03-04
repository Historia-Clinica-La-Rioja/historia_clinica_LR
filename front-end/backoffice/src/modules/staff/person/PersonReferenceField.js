import React from 'react';
import PropTypes from 'prop-types';
import {
    ReferenceField,
    FunctionField,
} from 'react-admin';

const renderPerson = (record) => `${record.identificationNumber ? record.identificationNumber : "" } ${record.lastName ? record.lastName : ""} ${record.firstName ? record.firstName : ""}`;

const PersonReferenceField = (props) => (
    <ReferenceField reference="person" link="show" {...props} >
        <FunctionField render={renderPerson}/>
    </ReferenceField>
);

PersonReferenceField.propTypes = {
    record: PropTypes.object,
};

export default PersonReferenceField;
