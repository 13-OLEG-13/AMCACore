package com.spleefleague.core.utils.inventorymenu;

import java.util.function.Consumer;




public class InventoryMenuTemplateBuilder extends InventoryMenuComponentTemplateBuilder<InventoryMenu,InventoryMenuTemplate,InventoryMenuTemplateBuilder>{

	private static final int ROWSIZE = 9;
	
	InventoryMenuTemplateBuilder(){
		
	}
	
	public InventoryMenuTemplateBuilder title(String title){
		buildingObj.setTitle(title);
		return this;
	}
	
	public InventoryMenuTemplateBuilder component(int x, int y,InventoryMenuItemTemplateBuilder itemTemplateBuilder){
		return component(x,y,itemTemplateBuilder.build());
	}
				
	public InventoryMenuTemplateBuilder component(int x, int y,InventoryMenuItemTemplate itemTemplate){
		return component(y * ROWSIZE + x,itemTemplate);
	}
	
	
	public InventoryMenuTemplateBuilder component(int position,InventoryMenuItemTemplateBuilder itemTemplateBuilder){
		return component(position,itemTemplateBuilder.build());
	}
	
	public InventoryMenuTemplateBuilder component(int position,InventoryMenuItemTemplate itemTemplate){
		buildingObj.addComponent(position, itemTemplate);
		return this;
	}
	
	
	public InventoryMenuTemplateBuilder component(int x, int y,InventoryMenuTemplateBuilder menuTemplateBuilder){
		return component(x,y,menuTemplateBuilder.build());
	}
	
	public InventoryMenuTemplateBuilder component(int x, int y,InventoryMenuTemplate menuTemplate){
		return component(y * ROWSIZE + x,menuTemplate);
	}
	
	
	public InventoryMenuTemplateBuilder component(int position,InventoryMenuTemplateBuilder menuTemplateBuilder){
		return component(position,menuTemplateBuilder.build());
	}
	
	public InventoryMenuTemplateBuilder component(int position,InventoryMenuTemplate menuTemplate){
		buildingObj.addComponent(position, menuTemplate);
		return this;
	}
	
	public InventoryMenuTemplateBuilder dynamicComponents(Consumer<InventoryMenuDynamicComponents> dynamic){
		buildingObj.dynamicComponents(dynamic);
		return this;
	}
	
	/*
	public InventoryMenuTemplateBuilder parent(InventoryMenuTemplate parent){
		buildingObj.setParent(parent);
		return this;
	}
	*/
	
	public InventoryMenuTemplateBuilder exitOnClickOutside(boolean exitOnClickOutside){
		buildingObj.setExitOnClickOutside(exitOnClickOutside);
		return this;
	}
	
	public InventoryMenuTemplateBuilder menuControls(boolean menuControls){
		buildingObj.setMenuControls(menuControls);
		return this;
	}

	@Override
	protected InventoryMenuTemplateBuilder getThis() {
		return this;
	}

	@Override
	protected InventoryMenuTemplate getObj() {
		return new InventoryMenuTemplate();
	}
}