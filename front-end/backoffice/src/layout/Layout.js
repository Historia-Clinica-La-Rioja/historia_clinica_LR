import * as React from 'react';
import { Layout } from 'react-admin';
import AppBar from './AppBar';

const MyLayout = (props) => {
    return (
        <Layout
            {...props}
            appBar={AppBar}
        />
    );
};

export default MyLayout;