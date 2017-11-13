package redispoc.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import redispoc.springframework.domain.Item;


	public interface ItemRepository extends CrudRepository<Item, String> {
	}
