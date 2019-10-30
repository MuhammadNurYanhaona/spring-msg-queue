package msgque.registry;

import msgque.domain.Peer;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class PeerRegistry {

	private Map<String, Peer> registry;

	public PeerRegistry() {
		registry = new HashMap<String, Peer>();
	}

	public Peer registerPeer(Peer peer) {
		return registry.put(peer.getUrl(), peer);
	}

	public Peer removePeer(Peer peer) {
		return registry.remove(peer.getUrl());
	}

	public Collection<Peer> getPeerList() {
		return registry.values();
	}

	public List<Peer> getAllExcept(Peer toExclude) {
		List<Peer> otherPeers = new ArrayList<Peer>();
		for (Peer peer : registry.values()) {
			if (!peer.getUrl().equals(toExclude.getUrl())) {
				otherPeers.add(peer);
			}
		}
		return otherPeers;
	}
}
