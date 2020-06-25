import React from 'react';
import PropTypes from 'prop-types';
import {
    ReferenceField,
    FunctionField,
} from 'react-admin';

const renderPeople = (record) => `${record.identificationNumber ? record.identificationNumber : "" } ${record.lastName ? record.lastName : ""} ${record.firstName ? record.firstName : ""}`;

const PeopleReferenceField = (props) => (
    <ReferenceField reference="people" link="show" {...props} >
        <FunctionField render={renderPeople}/>
    </ReferenceField>
);

PeopleReferenceField.propTypes = {
    record: PropTypes.object,
};

export default PeopleReferenceField;
