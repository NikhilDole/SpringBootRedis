package redispoc.springframework.services;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

import redispoc.springframework.commands.ItemForm;
import redispoc.springframework.converters.ItemFormToItem;
import redispoc.springframework.domain.Item;
import redispoc.springframework.repositories.ItemRepository;

import java.util.ArrayList;
	import java.util.List;

	@Service
	public class ItemServiceImpl implements ItemService {

	    private ItemRepository itemRepository;
	    private ItemFormToItem itemFormToItem;

	    @Autowired
	    public ItemServiceImpl(ItemRepository itemRepository, ItemFormToItem itemFormToItem) {
	        this.itemRepository = itemRepository;
	        this.itemFormToItem = itemFormToItem;
	    }


	    @Override
	    public List<Item> listAll() {
	        List<Item> items = new ArrayList<>();
	        itemRepository.findAll().forEach(items::add); 
	        return items;
	    }

	    @Override
	    public Item getById(String id) {
	        return itemRepository.findOne(id);
	    }

	    @Override
	    public Item saveOrUpdate(Item item) {
	        itemRepository.save(item);
	        return item;
	    }

	    @Override
	    public void delete(String id) {
	        itemRepository.delete(id);
	    }

	    @Override
	    public Item saveOrUpdateItemForm(ItemForm itemForm) {
	        Item savedItem = saveOrUpdate(itemFormToItem.convert(itemForm));
	        System.out.println("Saved Item Id: " + savedItem.getId());
	        return savedItem;
	    }
	}

