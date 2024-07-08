package store.buzzbook.corebatch.adapter;

import org.springframework.cloud.openfeign.FeignClient;


//todo uri변경
@FeignClient(name = "userAdapter", url = "http://${api.gateway.host}:" + "${api.gateway.port}/api/account")
public interface UserAdapter {
}
