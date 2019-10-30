package msgque;

import java.net.URL;
import java.net.MalformedURLException;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@Scope("singleton")
@Data
public class Host {

	@Value("${peer.communication.protocol=}")
	private String protocol;
	
	@Value("${host.ip.address}")
	private String ipAddress;

	@Value("${server.port}")
	private int port;

	@Value("${server.servlet.contextPath}")
	private String contextPath;

	public String getUrl() {
		try {
			URL url = new URL(protocol, ipAddress, port, contextPath);
			return url.toString();
		} catch (MalformedURLException ex) {
			return null;
		}
	}
}
