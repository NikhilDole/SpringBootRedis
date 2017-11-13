package redispoc.springframework.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redispoc.springframework.commands.ItemForm;
import redispoc.springframework.domain.Item;


	@Component
	public class ItemFormToItem implements Converter<ItemForm, Item> {

	    @Override
	    public Item convert(ItemForm itemForm) {
	        Item item = new Item();
	        if (itemForm.getId() != null  && !StringUtils.isEmpty(itemForm.getId())) {
	        	item.setId(itemForm.getId());
	        }
	        item.setDescription(itemForm.getDescription());
	        item.setPrice(itemForm.getPrice());

	        return item;
	    }
	}

