package ar.lamansys.sgx.shared.appnode.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemPropertyRepository;
import ar.lamansys.sgx.shared.appnode.infrastructure.output.repository.NodeData;
import ar.lamansys.sgx.shared.appnode.infrastructure.output.repository.NodeDataRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NodeService {

	private final AppNode appNode;
    private final Integer updateInterval;
    private final NodeDataRepository nodeDataRepository;
	private final SystemPropertyRepository systemPropertyRepository;


	public NodeService(
			@Value("${app.system.node-data.update-interval}") Integer updateInterval,
			NodeDataRepository nodeDataRepository,
			SystemPropertyRepository systemPropertyRepository,
			AppNode appNode) {
		this.updateInterval = updateInterval;
		this.nodeDataRepository = nodeDataRepository;
		this.systemPropertyRepository = systemPropertyRepository;
		this.appNode = appNode;
	}


	public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Error obteniendo hostname",e);
            return "_ERROR_";
        }
    }

    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
			log.error("Error obteniendo IP",e);
            return "_ERROR_";
        }
    }

	@Scheduled(fixedDelayString = "${app.system.node-data.update-interval}000")
    public void updateDBNodeData() {
        NodeData nodeData = new NodeData();
        nodeData.setUuid(appNode.nodeId);
        nodeData.setHostname(getHostname());
        nodeData.setIp(getIp());
        nodeData.setUpdated(LocalDateTime.now());
        nodeData.setInterval(updateInterval);

        nodeDataRepository.save(nodeData);

    }

	@Scheduled(fixedDelayString = "${app.system.node-data.clean-interval}000")
	public void cleanDBNodeData() {
		var outdatedNodes = nodeDataRepository.findAll().stream().filter(
				NodeData::isOutdated
		);
		outdatedNodes.forEach(outdatedNode -> {
			systemPropertyRepository.deleteByIpNodeId(outdatedNode.getUuid());
			nodeDataRepository.delete(outdatedNode);
		});
	}
}
