import React from 'react';
import { Handle, Position } from 'reactflow';

import './CustomNode.css';

/* 

  Nodo custom que muestra el nombre como titulo y luego todas las props
  que estan dentro de extraInfo <nombre_del_atributo>: <valor>

  {
    id: string
    data: {
      name: string,
      extraInfo: {
        todos los atributos que se quieran mostrar (strings o numbers)
      }
    }
  }

 */
function CustomNode({ id, data }) {
  return (
    <>
      <Handle type="target" position={Position.Bottom} id={`target-${id}`} />
      <div className="custom-node__header">
        <strong>{data.name}</strong>
      </div>
      <div className="custom-node__body">
        {
          Object.keys(data.extraInfo).map(
            (extraInfoKey) => (<ExtraInfoItem key={extraInfoKey} extraInfoKey={extraInfoKey} value={data.extraInfo[extraInfoKey]} />))
        }
      </div>
      <Handle type="source" position={Position.Top} id={`source-${id}`} />
    </>
  );
}

function ExtraInfoItem({ extraInfoKey, value }) {
  return (
    <div>
      <span >{extraInfoKey}: </span>
      <strong>{value}</strong>
    </div>
  )
}


export default CustomNode;
