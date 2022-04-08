import * as React from 'react';
import { Sidebar, Layout } from 'react-admin';
import { makeStyles } from '@material-ui/core/styles';

import { lightTheme } from './themes';
import AppBar from './AppBar';
import Menu from './Menu';

const useSidebarStyles = makeStyles({
    drawerPaper: {
        backgroundColor: '#fafafa',
        borderRight: '1px solid rgba(0,0,0,.12)',
    },
});

const MySidebar = props => {
    const classes = useSidebarStyles();
    return (
        <Sidebar classes={classes} {...props} />
    );
};

const MyLayout = (props) => {
    return (
        <Layout
            {...props}
            appBar={AppBar}
            menu={Menu}
            theme={lightTheme}
            sidebar={MySidebar}
        />
    );
};

export default MyLayout;