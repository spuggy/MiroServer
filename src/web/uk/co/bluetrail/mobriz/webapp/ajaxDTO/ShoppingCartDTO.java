package uk.co.bluetrail.mobriz.webapp.ajaxDTO;

import java.util.LinkedHashMap;





public class ShoppingCartDTO {

	private LinkedHashMap  items ;
	private int status   ;
	
	
	
	/**
	 * @hibernate.property 
	 * @return the items
	 */
	public LinkedHashMap getItems() {
		return items;
	}


	/**
	 * @hibernate.property 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	public void addToCart(String user_id,MiroCandidateAjaxDTO candidateDTO) {
		if(items==null) {
			items = new LinkedHashMap();
		}
		items.put(user_id, candidateDTO);
	}


	public void setStatus(int status) {
		this.status = status;
		
	}
	
	public int getCartCount() {
		if(items==null){
			return 0 ; 
		}
		
		return items.size();
	}


	public void delFromCart(String id) {
		
		try{
			items.remove(id);
		} catch (Exception e){
			//
		}
		
	}
	
	
}
