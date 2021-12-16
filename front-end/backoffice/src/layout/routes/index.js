import React from 'react';
import { Route } from 'react-router-dom';

import Home from './Home';

const Routes =  [
    <Route exact path="/home" component={Home} />,
];

export default Routes;
