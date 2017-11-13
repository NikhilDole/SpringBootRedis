package redispoc.springframework.services;

	import java.util.List;

import redispoc.springframework.commands.ItemForm;
import redispoc.springframework.domain.Item;


	public interface ItemService {

	    List<Item> listAll();

	    Item getById(String id);

	    Item saveOrUpdate(Item item);

	    void delete(String id);

	    Item saveOrUpdateItemForm(ItemForm itemForm);
	}
