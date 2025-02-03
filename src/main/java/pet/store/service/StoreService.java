package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class StoreService {

	@Autowired
	PetStoreDao petStoreDao;

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData saveStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreateStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> results = new LinkedList<PetStoreData>();

		for (PetStore store : petStores) {
			results.add(new PetStoreData(store));
		}

		return results;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrieveStoreById(Long storeId) {
		return new PetStoreData(findStoreById(storeId));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setCity(petStoreData.getCity());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreateStore(Long petStoreId) {
		PetStore petStore;

		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " was not found."));
	}

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(PetStoreEmployee employeeData, Long storeId) {
		PetStore petStore = findStoreById(storeId);
		Long employeeId = employeeData.getEmployeeId();
		Employee employee = findOrCreateEmployee(employeeId, storeId);

		copyEmployeeFields(employee, employeeData);
		relateStoreAndEmployee(petStore, employee);

		return new PetStoreEmployee(employeeDao.save(employee));
	}

	@Transactional(readOnly = true)
	public List<PetStoreEmployee> retrieveAllEmployees(Long storeId) {
		PetStore petStore = findStoreById(storeId);
		List<PetStoreEmployee> results = new LinkedList<PetStoreEmployee>();
		for (Employee employee : petStore.getEmployees()) {
			results.add(new PetStoreEmployee(employee));
		}
		return results;
	}

	@Transactional(readOnly = true)
	public PetStoreEmployee retrieveEmployeeById(Long storeId, Long employeeId) {
		findStoreById(storeId);
		Employee employee = findEmployeeById(employeeId, storeId);
		return new PetStoreEmployee(employee);
	}

	private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
		Employee employee;

		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(employeeId, petStoreId);
		}
		return employee;
	}

	private Employee findEmployeeById(Long employeeId, Long petStoreId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));

		PetStore employeeStore = employee.getPetStore();
		if (Objects.nonNull(employeeStore) && employeeStore.getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException(
					"Employee with ID=" + employeeId + " is not owned by store with ID=" + petStoreId);
		}
		return employee;
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee employeeData) {
		employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
		employee.setEmployeeId(employeeData.getEmployeeId());
		employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());
		employee.setEmployeeLastName(employeeData.getEmployeeLastName());
		employee.setEmployeePhone(employeeData.getEmployeePhone());
	}

	private void relateStoreAndEmployee(PetStore petStore, Employee employee) {
		petStore.getEmployees().add(employee);
		employee.setPetStore(petStore);
	}

	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(PetStoreCustomer customerData, Long storeId) {
		PetStore petStore = findStoreById(storeId);
		Long customerId = customerData.getCustomerId();
		Customer customer = findOrCreateCustomer(customerId);

		copyCustomerFields(customer, customerData);
		relateStoreAndCustomer(petStore, customer);

		return new PetStoreCustomer(customerDao.save(customer));
	}
	
	@Transactional(readOnly = false)
	public PetStoreCustomer attachCustomertoStore(Long storeId, Long customerId) {
		PetStore petStore = findStoreById(storeId);
		Customer customer = findCustomerById(customerId);
		relateStoreAndCustomer(petStore, customer);
		return new PetStoreCustomer(customerDao.save(customer));
	}

	private Customer findOrCreateCustomer(Long customerId) {
		Customer customer;

		if (Objects.isNull(customerId)) {
			customer = new Customer();
		} else {
			customer = findCustomerById(customerId);
		}
		return customer;
	}

	private Customer findCustomerById(Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
		return customer;
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer customerData) {
		customer.setCustomerEmail(customerData.getCustomerEmail());
		customer.setCustomerFirstName(customerData.getCustomerFirstName());
		customer.setCustomerId(customerData.getCustomerId());
		customer.setCustomerLastName(customerData.getCustomerLastName());
	}

	private void relateStoreAndCustomer(PetStore petStore, Customer customer) {
		petStore.getCustomers().add(customer);
		customer.getPetStores().add(petStore);
	}

	@Transactional(readOnly = false)
	public Map<String, String> deleteStore(Long storeId) {
		PetStore petStore = findStoreById(storeId);
		petStoreDao.delete(petStore);
		return Map.of("message", "Pet store with ID= " + storeId + " was successfully deleted");
	}

}
