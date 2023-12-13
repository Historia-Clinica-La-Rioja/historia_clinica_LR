import React, { useRef } from 'react';
import ReactFlow, {
    ReactFlowProvider,
    MiniMap,
    Controls,
    Background
} from 'reactflow';
import 'reactflow/dist/style.css';

import './AddNodeOnEdgeDrop.css';
import CustomNode from './CustomNode';


const nodeTypes = {
    custom: CustomNode,
};
const minimapStyle = {
    height: 120,
};

/*
        **** AddNodeOnEdgeDrop ==> Agregar nodo al soltar el puente ****
** Es un wrapper que se le agrega a la libreria para poder hacer drag y create de nodos.
** En principio no se usa pero queda para cuando se quieran crear nuevos nodos desde el grafico.
** Hay que agregar unas funciones cuando se quiera hacer esta funcionalidad

** https://reactflow.dev/docs/examples/nodes/add-node-on-edge-drop/

   Los nodos deben estar ordenados* para que el grafico se visualice correctamente.
   Es decir, si un nodoA es padre de un nodoB, el nodoA tiene que estar antes en la lista de nodos.
*/
const AddNodeOnEdgeDrop = (props) => {
    let nodes = props.nodes;
    let edges = props.edges;
    let onNodesChange = props.onNodesChange
    let onEdgesChange = props.onEdgesChange

    const reactFlowWrapper = useRef(null);

    return (
        <div className="wrapper" ref={reactFlowWrapper}>
            <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                fitView
                nodeTypes={nodeTypes}
            >
                <MiniMap style={minimapStyle} zoomable pannable />
                <Controls />
                <Background color="#aaa" gap={16} />
            </ReactFlow>
        </div>
    );
};



export const Graph = (props) => (
    <ReactFlowProvider>
        <AddNodeOnEdgeDrop {...props} />
    </ReactFlowProvider>
);
