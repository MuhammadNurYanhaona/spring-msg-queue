package msgque.comm;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CommonResponse {
	
	// for any response type, TRUE means that the corresponding
	// request has been accepted as valid and FALSE means that
	// the request was either errorneous or its processing has
	// failed for some reason. 
	protected boolean status;

	// if the request is processed successfully then there will be
	// no failerReason in the response. Otherwise, the error 
	// message here will explain why the request has not been
	// successful.
	protected String failerReason; 
}
