package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
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

	@PutMapping("/store/{id}")
	public PetStoreData updatePetStore(@RequestBody PetStoreData petStoreData, @PathVariable Long id) {
		petStoreData.setPetStoreId(id);
		log.info("Updating store {}", petStoreData);
		return storeService.saveStore(petStoreData);
	}

}
