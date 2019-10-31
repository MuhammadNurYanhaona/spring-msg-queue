package msgque.comm.client;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import msgque.domain.Subscription;
import msgque.comm.CommonResponse;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class SubscriptionResponse extends CommonResponse {
	Subscription subscription;
}
