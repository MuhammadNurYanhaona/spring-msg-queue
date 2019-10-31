package msgque.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Peer {
	String url;
}
