package br.jus.tjpb.requisitos.entidade;

public class TicketRedmine {
	
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public int getIdProjeto() {
		return idProjeto;
	}
	public void setIdProjeto(int idProjeto) {
		this.idProjeto = idProjeto;
	}
	public String getNomeProjeto() {
		return nomeProjeto;
	}
	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}
	public int getIdTipoTicket() {
		return idTipoTicket;
	}
	public void setIdTipoTicket(int idTipoTicket) {
		this.idTipoTicket = idTipoTicket;
	}
	public String getTipoTicket() {
		return tipoTicket;
	}
	public void setTipoTicket(String tipoTicket) {
		this.tipoTicket = tipoTicket;
	}
	public int getIdVersao() {
		return idVersao;
	}
	public void setIdVersao(int idVersao) {
		this.idVersao = idVersao;
	}
	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
	public int getPercTerminado() {
		return percTerminado;
	}
	public void setPercTerminado(int percTerminado) {
		this.percTerminado = percTerminado;
	}
	public int getEsfocoAnalise() {
		return esfocoAnalise;
	}
	public void setEsfocoAnalise(int esfocoAnalise) {
		this.esfocoAnalise = esfocoAnalise;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	@Override
	public String toString() {
		return "TicketRedmine [ticket=" + ticket + ", idProjeto=" + idProjeto + ", nomeProjeto=" + nomeProjeto
				+ ", idTipoTicket=" + idTipoTicket + ", tipoTicket=" + tipoTicket + ", idVersao=" + idVersao
				+ ", versao=" + versao + ", percTerminado=" + percTerminado + ", esfocoAnalise=" + esfocoAnalise + "]";
	}

	private int ticket;
	private int idProjeto;
	private String nomeProjeto;
	private int idTipoTicket;
	private String tipoTicket;
	private int idVersao;
	private String versao;
	private int percTerminado;
	private int esfocoAnalise;
	private String titulo;
}
