package msgque.comm.peer;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ConnectRequest {
	private String url;
}
