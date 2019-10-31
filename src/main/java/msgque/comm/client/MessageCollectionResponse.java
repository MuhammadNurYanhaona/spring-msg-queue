package msgque.comm.client;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import java.util.List;
import msgque.domain.Message;
import msgque.comm.CommonResponse;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class MessageCollectionResponse extends CommonResponse {
	List<Message> messages;
}
