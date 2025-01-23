package pet.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class City {
	@Column(name = "pet_store_city")
	private String city;
	@Column(name = "pet_store_state")
	private String state;
	@Column(name = "pet_store_zip")
	private String zip;

	public City(String city, String state, String zip) {
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
}
