package store.buzzbook.corebatch.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import store.buzzbook.corebatch.dto.user.UserRealBill;

//todo uri변경
@FeignClient(name = "userAdapter", url = "http://${api.gateway.host}:" + "${api.gateway.port}/api/account")
public interface UserAdapter {

	@GetMapping("/bills/3month")
	ResponseEntity<List<UserRealBill>> get3MonthBills();
}
