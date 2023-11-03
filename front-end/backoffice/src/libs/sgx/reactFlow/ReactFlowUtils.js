import { MarkerType, useNodesState, useEdgesState } from 'reactflow';

export function GetPropsFromSortedNodes(sortedNodes) {
  const sortedCoordinatedNodes = setCoordinatesFromSortedNodes(sortedNodes);
  let [nodes, setNodes, onNodesChange] = useNodesState(sortedCoordinatedNodes);

  const calculatedEdges = getEdges(sortedCoordinatedNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(calculatedEdges);

  return { nodes, setNodes, onNodesChange, edges, setEdges, onEdgesChange }
}

function getEdges(nodes) {
  const edges = nodes.map(node => getNodeEdges(node));
  return edges.flat(1);
}

function getNodeEdges(node) {
  return node.parents.map(
    parent => {
      return {
        id: `edge-${node.id}-${parent}`,
        source: node.id.toString(),
        target: parent.toString(),
        markerEnd: {
          type: MarkerType.ArrowClosed,
        }
      }
    }
  )
}

function setCoordinatesFromSortedNodes(nodos) {
  const coordenadas = {};
  const niveles = [[]];

  // Función auxiliar para obtener el nivel de un nodo
  function obtenerNivel(nodoId) {
    if (!coordenadas[nodoId]) return 0;
    return coordenadas[nodoId].nivel;
  }

  nodos.forEach(nodo => {
    const nivel = nodo.parents.reduce((nivelMax, parent) => {
      const parentNivel = obtenerNivel(parent);
      return Math.max(nivelMax, parentNivel);
    }, 0) + 1;

    if (!niveles[nivel]) niveles[nivel] = [];
    niveles[nivel].push(nodo);
    coordenadas[nodo.id] = { x: 0, y: nivel * 200, nivel };
  });

  niveles.forEach(nivel => {
    const nivelCount = nivel.length;
    const nivelCenter = (nivelCount - 1) / 2;
    let x = -nivelCenter * 200;
    nivel.forEach(nodo => {
      coordenadas[nodo.id].x = x;
      x += 200;
    });
  });

  niveles.forEach((nivel, index) => {
    const nivelCount = nivel.length;
    const nivelCenter = (nivelCount - 1) / 2;
    nivel.forEach((nodo, nodoIndex) => {
      const offsetX = (nodoIndex - nivelCenter) * 200;
      coordenadas[nodo.id].x += offsetX;
    });
  });

  return nodos.map(nodo => ({ ...nodo, position: coordenadas[nodo.id] }));
}
