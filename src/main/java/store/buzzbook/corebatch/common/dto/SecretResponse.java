package store.buzzbook.corebatch.common.dto;

import lombok.Getter;

@Getter
public class SecretResponse {
	private Body body;

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
