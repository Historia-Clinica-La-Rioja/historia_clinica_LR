import React from "react";
import { GetPropsFromSortedNodes } from './ReactFlowUtils.js';
import { Graph } from './AddNodeOnEdgeDrop.js';

/* 
    Este componente actua unicamente de mapper.
    Pasa la info a como la requiere la libreria(nodos y puentes por separados).
*/
function Flow(props) {
    const propsToSet = GetPropsFromSortedNodes(props.sortedNodes);
    return <Graph {...propsToSet} />;
}

export default Flow;