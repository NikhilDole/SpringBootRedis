package redispoc.springframework.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import redispoc.springframework.commands.ItemForm;
import redispoc.springframework.domain.Item;


	@Component
	public class ItemToItemForm implements Converter<Item, ItemForm> {
	    @Override
	    public ItemForm convert(Item item) {
	        ItemForm itemForm = new ItemForm();
	        itemForm.setId(item.getId());
	        itemForm.setDescription(item.getDescription());
	        itemForm.setPrice(item.getPrice());

	        return itemForm;
	    }
	}
