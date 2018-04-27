package br.jus.tjpb.requisitos.entidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class CardTrello {

	private String id;
	private String idList;
	private String name;
	private String shortLink;
	private DateTime dataEntrega;
	private boolean cardDataFinalizada;
	private DateTime dataUltimaAtividade;
	private String descricao;
	private Map<String, String> descricoes;
	
	private double apfEstimada;
	private double apfDetalhada;
	
	private double pesoEspecificado;
	private double pesoTotal;
	
	private int tempoAtivo;
	private int tempoAtivoSemFimDeSemana;
	
	private ArrayList<AcaoCard> listaAcoes;
	
	private ArrayList<String> listaMembros;
	
	private List<TicketRedmine> ticketsRedmine;
	private BigDecimal percentualTotal;
	
	public CardTrello() {
		listaAcoes = new ArrayList<AcaoCard>();
		descricoes = new HashMap<String, String>();
		listaMembros = new ArrayList<String>();
		this.pesoTotal = 0;
		this.pesoEspecificado = 0;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdList() {
		return idList;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortLink() {
		return shortLink;
	}

	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}

	public DateTime getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(DateTime dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public boolean isCardDataFinalizada() {
		return cardDataFinalizada;
	}

	public void setCardDataFinalizada(boolean cardDataFinalizada) {
		this.cardDataFinalizada = cardDataFinalizada;
	}

	public DateTime getDataUltimaAtividade() {
		return dataUltimaAtividade;
	}

	public void setDataUltimaAtividade(DateTime dataUltimaAtividade) {
		this.dataUltimaAtividade = dataUltimaAtividade;
	}

	public void addAcao(AcaoCard acao) {
		listaAcoes.add(acao);
	}
	
	public ArrayList<AcaoCard> getListaAcoes() {
		return listaAcoes;
	}
	
	@Override
	public String toString() {
		return "CardTrello [id=" + id + ", idList=" + idList + ", name=" + name + ", shortLink=" + shortLink
				+ ", dataEntrega=" + dataEntrega + ", cardDataFinalizada=" + cardDataFinalizada
				+ ", dataUltimaAtividade=" + dataUltimaAtividade + ", descricoes =" + descricoes  
				+ ", APFEstimada=" + apfEstimada + ", APFDetalhada=" + apfDetalhada + ", TempoAtivo="+tempoAtivo
				+ ", Membros="+listaMembros +"]";
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Map<String, String> getDescricoes() {
		return descricoes;
	}

	public void setDescricoes(Map<String, String> descricoes) {
		this.descricoes = descricoes;
	}

	public double getApfEstimada() {
		return apfEstimada;
	}

	public void setApfEstimada(double apfEstimada) {
		this.apfEstimada = apfEstimada;
	}

	public double getApfDetalhada() {
		return apfDetalhada;
	}

	public void setApfDetalhada(double apfDetalhada) {
		this.apfDetalhada = apfDetalhada;
	}

	public int getTempoAtivo() {
		return tempoAtivo;
	}

	public void setTempoAtivo(int tempoAtivo) {
		this.tempoAtivo = tempoAtivo;
	}

	public int getTempoAtivoSemFimDeSemana() {
		return tempoAtivoSemFimDeSemana;
	}

	public void setTempoAtivoSemFimDeSemana(int tempoAtivoSemFimDeSemana) {
		this.tempoAtivoSemFimDeSemana = tempoAtivoSemFimDeSemana;
	}

	public List<TicketRedmine> getTicketsRedmine() {
		return ticketsRedmine;
	}

	public void setTicketsRedmine(List<TicketRedmine> tickets) {
		this.ticketsRedmine = tickets;
	}

	public double getPesoEspecificado() {
		return pesoEspecificado;
	}

	public void setPesoEspecificado(double pesoEspecificado) {
		this.pesoEspecificado = pesoEspecificado;
	}

	public double getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(double pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public ArrayList<String> getListaMembros() {
		return listaMembros;
	}

	public void addMembro(String membro) {
		this.listaMembros.add(membro);
	}

	public BigDecimal getPercentualTotal() {
		return percentualTotal;
	}

	public void setPercentualTotal(BigDecimal percentualTotal) {
		this.percentualTotal = percentualTotal;
	}

	private void calcularPesos() {
		this.pesoTotal = 0;
		this.pesoEspecificado = 0;
		for (int i = 0; i < ticketsRedmine.size(); i++) {
			pesoTotal += ticketsRedmine.get(i).getEsfocoAnalise();
			if(ticketsRedmine.get(i).getPercTerminado() >=40) {
				pesoEspecificado += ticketsRedmine.get(i).getEsfocoAnalise();
			}
		}
	}
	
	public void calcularPercentualTotal() {
		calcularPesos();
		if(pesoTotal != 0)
			setPercentualTotal(new BigDecimal(pesoEspecificado).divide(new BigDecimal(pesoTotal), 2, RoundingMode.HALF_UP));
		else setPercentualTotal(new BigDecimal(0.0));
	}
	
}
