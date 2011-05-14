package uk.co.bluetrail.mobriz.webapp.ajax;

import java.util.Vector;

import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroCandidateAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.ShoppingCartDTO;

public interface AjaxMiroProjectManager {

	public MiroCandidateAjaxDTO[] getCandidates(String projectId) throws Exception ;
	public MiroCandidateAjaxDTO saveCandidate(MiroCandidateAjaxDTO miroCandidateAjaxDTO) throws Exception ;
	public MiroCandidateAjaxDTO deleteCandidate(String candidateId) throws Exception;
	public Object[] sendInviteEmails(String[]  ids);
	public MiroCandidateAjaxDTO buyReport(String id);
	public ShoppingCartDTO addToCart(String id);
	public MiroCandidateAjaxDTO[] getShoppingCartItems();
}
