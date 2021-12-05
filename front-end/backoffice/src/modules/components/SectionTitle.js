import React from 'react';
import {
    useTranslate,
} from 'react-admin';
import { Typography } from '@material-ui/core';

//Taken from examples/demo/VisitorCreate
const SectionTitle = ({ label }) => {
    const translate = useTranslate();

    return (
        <Typography variant="h6" gutterBottom>
            {translate(label)}
        </Typography>
    );
};

export default SectionTitle;
