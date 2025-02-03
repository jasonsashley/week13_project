package pet.store.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.StoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class StoreController {

	@Autowired
	StoreService storeService;

	@PostMapping("/store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating store {}", petStoreData);
		return storeService.saveStore(petStoreData);
	}

	@PutMapping("/store/{storeId}")
	public PetStoreData updatePetStore(@RequestBody PetStoreData petStoreData, @PathVariable Long storeId) {
		petStoreData.setPetStoreId(storeId);
		log.info("Updating store {}", petStoreData);
		return storeService.saveStore(petStoreData);
	}

	@GetMapping("/store")
	public List<PetStoreData> retrieveAllStores() {
		log.info("Retrieving all pet stores.");
		return storeService.retrieveAllStores();
	}

	@GetMapping("/store/{storeId}")
	public PetStoreData retrieveStoreById(@PathVariable Long storeId) {
		log.info("Retrieving pet store with ID={}", storeId);
		return storeService.retrieveStoreById(storeId);
	}
	
	@DeleteMapping("/store/{storeId}")
	public Map<String, String> deleteStore(@PathVariable Long storeId) {
		log.info("Deleting pet store with ID={}", storeId);
		return storeService.deleteStore(storeId);
	}

	@PostMapping("/store/{storeId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployee(@RequestBody PetStoreEmployee employeeData, @PathVariable Long storeId) {
		log.info("Creating employee {} for store with ID={}", employeeData, storeId);
		return storeService.saveEmployee(employeeData, storeId);
	}

	@PutMapping("/store/{storeId}/employee/{employeeId}")
	public PetStoreEmployee updateEmployee(@RequestBody PetStoreEmployee employeeData, @PathVariable Long storeId,
			@PathVariable Long employeeId) {
		log.info("Updating employee {} for store with ID={}", employeeData, storeId);
		employeeData.setEmployeeId(employeeId);
		return storeService.saveEmployee(employeeData, storeId);
	}

	@GetMapping("/store/{storeId}/employee")
	public List<PetStoreEmployee> retrieveAllEmployees(@PathVariable Long storeId) {
		log.info("Retrieving all employees for store with ID={}", storeId);
		return storeService.retrieveAllEmployees(storeId);
	}

	@GetMapping("/store/{storeId}/employee/{employeeId}")
	public PetStoreEmployee retrieveEmployeeById(@PathVariable Long storeId, @PathVariable Long employeeId) {
		log.info("Retrieving employee with ID={} for store with ID={}", employeeId, storeId);
		return storeService.retrieveEmployeeById(storeId, employeeId);
	}

	@PostMapping("/store/{storeId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomer(@RequestBody PetStoreCustomer customerData, @PathVariable Long storeId) {
		log.info("Creating customer {} for store with ID={}", customerData, storeId);
		return storeService.saveCustomer(customerData, storeId);
	}

	@PutMapping("/store/{storeId}/customer/{customerId}")
	public PetStoreCustomer updateCustomer(@RequestBody(required = false) PetStoreCustomer customerData,
			@PathVariable Long storeId, @PathVariable Long customerId) {

		if (Objects.isNull(customerData)) {
			log.info("Attaching customer with ID={} to store with ID={}", customerId, storeId);
			return storeService.attachCustomertoStore(storeId, customerId);
		}
		log.info("Updating customer {} for store with ID={}", customerData, storeId);
		customerData.setCustomerId(customerId);
		return storeService.saveCustomer(customerData, storeId);

	}

}
