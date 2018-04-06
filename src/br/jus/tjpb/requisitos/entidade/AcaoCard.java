package br.jus.tjpb.requisitos.entidade;

import org.joda.time.DateTime;


public class AcaoCard {
	private String idAcao;
	private String idCartao;
	private String idCriador;
	private String idListaAntes;
	private String listaAntes;
	private String idListaDepois;
	private String listaDepois;
	private DateTime dataOcorrencia;
	
	public String getIdAcao() {
		return idAcao;
	}
	public void setIdAcao(String idAcao) {
		this.idAcao = idAcao;
	}
	public String getIdCartao() {
		return idCartao;
	}
	public void setIdCartao(String idCartao) {
		this.idCartao = idCartao;
	}
	public String getIdCriador() {
		return idCriador;
	}
	public void setIdCriador(String idCriador) {
		this.idCriador = idCriador;
	}
	public String getIdListaAntes() {
		return idListaAntes;
	}
	public void setIdListaAntes(String idListaAntes) {
		this.idListaAntes = idListaAntes;
	}
	public String getIdListaDepois() {
		return idListaDepois;
	}
	public void setIdListaDepois(String idListaDepois) {
		this.idListaDepois = idListaDepois;
	}
	
	public DateTime getDataOcorrencia() {
		return dataOcorrencia;
	}
	public void setDataOcorrencia(DateTime dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}
	public String getListaAntes() {
		return listaAntes;
	}
	public void setListaAntes(String listaAntes) {
		this.listaAntes = listaAntes;
	}
	public String getListaDepois() {
		return listaDepois;
	}
	public void setListaDepois(String listaDepois) {
		this.listaDepois = listaDepois;
	}
	@Override
	public String toString() {
		return "AcaoCard [idAcao=" + idAcao + ", idCartao=" + idCartao + ", idCriador=" + idCriador + ", ListaAntes="
				+ listaAntes + ", ListaDepois=" + listaDepois + ", dataOcorrencia=" + dataOcorrencia +"]";
	}
	
	
}
