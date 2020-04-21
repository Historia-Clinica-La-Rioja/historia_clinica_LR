import React from 'react';
import {
    ReferenceField,
} from 'react-admin';

//See: https://stackoverflow.com/a/53161132/2442481
//This hack can show fields 2 steps away from the resource
//e.g. Your model is person->address->city and you need to show a person's city
const SubReference = ({ translateChoice, children, ...props }) => (
    <ReferenceField {...props}>{children}</ReferenceField>
);

export default SubReference;
