package de.vsfexperts.rbac.sample.rest;

import static de.vsfexperts.rbac.sample.Privileges.DELETE_ORDERS;
import static de.vsfexperts.rbac.sample.Privileges.LIST_ORDERS;
import static de.vsfexperts.rbac.sample.Privileges.UPDATE_ORDERS;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple sample controller in order to provide some protected endpoints
 */
@RestController
public class OrderController {

	@GetMapping(value = "/order", produces = "text/plain")
	@Secured(LIST_ORDERS)
	public String listOrders() {
		return "123\n456";
	}

	@PostMapping(value = "/order", produces = "text/plain")
	@Secured(UPDATE_ORDERS)
	public String updateOrder(@RequestParam(value = "theOrder") final String order) {
		return "Order was updated with value \"" + order + "\"";
	}

	@DeleteMapping(value = "/order/{orderId}", produces = "text/plain")
	@Secured(DELETE_ORDERS)
	public String deleteOrder(@PathVariable("orderId") final String orderId) {
		return "Order " + orderId + " was deleted";
	}

}
