package br.jus.tjpb.requisitos.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import br.jus.tjpb.requisitos.entidade.AcaoCard;
import br.jus.tjpb.requisitos.entidade.CardTrello;
import br.jus.tjpb.requisitos.entidade.TicketRedmine;
import br.jus.tjpb.requisitos.servico.ClienteTrello;

@ManagedBean(name="dataTrello")
@ViewScoped
public class RelTrello implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<CardTrello> listaCards;
	private CardTrello selectedCard;
	private List<TicketRedmine> selectedListaRedmine;
	
	@ManagedProperty("#{clienteTrello}")
	private ClienteTrello cliente;
	
	private List<AcaoCard> listaAcoes;
	
	private Map<String, String> listas_ativas;
	
	private Map<String, String> lista_membros;

	private ArrayList<String> membrosInativos;
	
	@PostConstruct
	public void init() {
		listas_ativas = new LinkedHashMap<String, String>();
		listas_ativas.put("56cf6e735694b3d802e8d8a0", "Elicitação de requisitos");
		listas_ativas.put("56cf6e8ef40ca6ca3bcbe26f", "Documentação");
		listas_ativas.put("56cf6e9ae769951207321a17", "Contagem APF detalhada");
		listas_ativas.put("598362c41c81f436c57b99d8", "Contagem APF Aplicação");
		
		lista_membros = new LinkedHashMap<String, String>();
		lista_membros.put("565e14e732bb6ebfecdb310d", "Cassio Higino de Freitas");
		lista_membros.put("5728e7427fd159d4b7eb04a2", "Daniel Medeiros");
		lista_membros.put("56b398f876daf28d1b1fe530", "Daniel Melo");
		lista_membros.put("5728fb3cc0fce24fa49fd177", "Fabricio Oliveira");
		lista_membros.put("56b4c47c85cce54aa6cb78d2", "Luciano Medeiros");
		lista_membros.put("56b3a60a927dcaa8a05413a2", "MARCELLO GALDINO PASSOS");
		lista_membros.put("56b484da81b889f20fb16df8", "Raphael Porto");
		
		buscarCards();
		buscarMembrosInativos();
	}

	public List<CardTrello> getListaCards() {
		return listaCards;
	}

	public void setListaCards(List<CardTrello> listaCards) {
		this.listaCards = listaCards;
	}
	
	public CardTrello getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(CardTrello selectedCard) {
		this.selectedCard = selectedCard;
	}

	public ClienteTrello getCliente() {
		return cliente;
	}

	public void setCliente(ClienteTrello cliente) {
		this.cliente = cliente;
	}

	public List<AcaoCard> getListaAcoes() {
		return listaAcoes;
	}

	public void setListaAcoes(List<AcaoCard> listaAcoes) {
		this.listaAcoes = listaAcoes;
	}

	public Map<String, String> getListas_ativas() {
		return listas_ativas;
	}

	public void setListas_ativas(Map<String, String> listas_ativas) {
		this.listas_ativas = listas_ativas;
	}

	public ArrayList<String> getMembrosInativos() {
		return membrosInativos;
	}

	public void setMembrosInativos(ArrayList<String> membrosInativos) {
		this.membrosInativos = membrosInativos;
	}

	public Map<String, String> getLista_membros() {
		return lista_membros;
	}

	public void setLista_membros(Map<String, String> lista_membros) {
		this.lista_membros = lista_membros;
	}

	public List<TicketRedmine> getSelectedListaRedmine() {
		return selectedListaRedmine;
	}

	public void setSelectedListaRedmine(List<TicketRedmine> selectedListaRedmine) {
		this.selectedListaRedmine = selectedListaRedmine;
	}

	public void onRowSelect(SelectEvent event) {
		//listaAcoes = ((CardTrello) event.getObject()).getListaAcoes();
		listaAcoes = selectedCard.getListaAcoes();
    }
	
	public void onRowSelectRedmine(SelectEvent event) {
		selectedListaRedmine = selectedCard.getTicketsRedmine();
	}
	
	public void buscarCards() {
		listaCards = cliente.getCards(listas_ativas.keySet().toArray());
	}
	
	public void buscarMembrosInativos() {
		
		ArrayList<String> membrosAtivos = new ArrayList<String>();
		for (int i = 0; i < listaCards.size(); i++) {
			membrosAtivos.addAll(listaCards.get(i).getListaMembros());
		}
		
		Set<String> membros = lista_membros.keySet();
		
		this.membrosInativos = new ArrayList<String>();
		
		for (Iterator<String> iterator = membros.iterator(); iterator.hasNext();) {
			String membro = (String) iterator.next();
			if(!membrosAtivos.contains(membro)) {
				this.membrosInativos.add(membro);
			}
		}
	}

}
