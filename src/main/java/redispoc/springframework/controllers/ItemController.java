package redispoc.springframework.controllers;

	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.validation.BindingResult;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;

import groovy.lang.Grab;
import redispoc.springframework.commands.ItemForm;
import redispoc.springframework.converters.ItemToItemForm;
import redispoc.springframework.domain.Item;
import redispoc.springframework.services.ItemService;

import javax.validation.Valid;

	@Grab("spring-boot-starter-security")
	@EnableOAuth2Sso
	@Controller
	public class ItemController {
	    private ItemService itemService;

	    private ItemToItemForm itemToItemForm;

	    @Autowired
	    public void setItemToItemForm(ItemToItemForm itemToItemForm) {
	        this.itemToItemForm = itemToItemForm;
	    }

	    @Autowired
	    public void setItemService(ItemService itemService) {
	        this.itemService = itemService;
	    }

	    @RequestMapping("/")
	    public String redirToList(){
	        return "redirect:/item/list";
	    }

	    @RequestMapping({"/item/list", "/item"})
	    public String listItems(Model model){
	        model.addAttribute("items", itemService.listAll());
	        return "item/list";
	    }

	    @RequestMapping("/item/show/{id}")
	    public String getItem(@PathVariable String id, Model model){
	        model.addAttribute("item", itemService.getById(id));
	        return "item/show";
	    }

	    @RequestMapping("item/edit/{id}")
	    public String edit(@PathVariable String id, Model model){
	        Item item = itemService.getById(id);
	        ItemForm itemForm = itemToItemForm.convert(item);

	        model.addAttribute("itemForm", itemForm);
	        return "item/itemform";
	    }

	    @RequestMapping("/item/new")
	    public String newItem(Model model){
	        model.addAttribute("itemForm", new ItemForm());
	        return "item/itemform";
	    }

	    @RequestMapping(value = "/item", method = RequestMethod.POST)
	    public String saveOrUpdateItem(@Valid ItemForm itemForm, BindingResult bindingResult){

	        if(bindingResult.hasErrors()){
	            return "item/itemform";
	        }

	        Item savedProduct = itemService.saveOrUpdateItemForm(itemForm);

	        return "redirect:/item/show/" + savedProduct.getId();
	    }

	    @RequestMapping("/item/delete/{id}")
	    public String delete(@PathVariable String id){
	        itemService.delete(id);
	        return "redirect:/item/list";
	    }
	}

