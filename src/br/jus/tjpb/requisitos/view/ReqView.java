package br.jus.tjpb.requisitos.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.jus.tjpb.requisitos.entidade.TicketRedmine;
import br.jus.tjpb.requisitos.servico.ClienteRedmine;

@ManagedBean(name="dataReqView")
@ViewScoped
public class ReqView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TicketRedmine> tickets;
	private double pesoTotal;
	private double pesoEspecificado;
	
	@ManagedProperty("#{clienteRedmine}")
	private ClienteRedmine cliente;

	@PostConstruct
	public void init() {
		initFormEntrada();
	}
	
	private void calcularPesos() {
		this.pesoTotal = 0;
		this.pesoEspecificado = 0;
		for (int i = 0; i < tickets.size(); i++) {
			pesoTotal += tickets.get(i).getEsfocoAnalise();
			if(tickets.get(i).getPercTerminado() >=40) {
				pesoEspecificado += tickets.get(i).getEsfocoAnalise();
			}
		}
	}
	
	public double getPercentualTotal() {
		if(pesoTotal != 0)
			return pesoEspecificado/pesoTotal;
		else return 0;
	}
	
	public List<TicketRedmine> getTickets(){
		return this.tickets;
	}
	
	public void setCliente(ClienteRedmine cliente) {
		this.cliente = cliente;
	}

	public double getPesoTotal() {
		return pesoTotal;
	}

	public double getPesoEspecificado() {
		return pesoEspecificado;
	}
	
	
	
	
	public Map<Integer, Map<String, Integer>> getData() {
		return data;
	}

	public void setData(Map<Integer, Map<String, Integer>> data) {
		this.data = data;
	}

	public Integer getProjetoEntrada() {
		return projetoEntrada;
	}

	public void setProjetoEntrada(Integer projetoEntrada) {
		this.projetoEntrada = projetoEntrada;
	}

	public Integer getVersaoEntrada() {
		return versaoEntrada;
	}

	public void setVersaoEntrada(Integer versaoEntrada) {
		this.versaoEntrada = versaoEntrada;
	}

	public Map<String, Integer> getProjetos() {
		return projetos;
	}

	public void setProjetos(Map<String, Integer> projetos) {
		this.projetos = projetos;
	}

	public Map<String, Integer> getVersoes() {
		return versoes;
	}

	public void setVersoes(Map<String, Integer> versoes) {
		this.versoes = versoes;
	}




	private Map<Integer,Map<String, Integer>> data = new HashMap<Integer,Map<String, Integer>>();
    private Integer projetoEntrada; 
    private Integer versaoEntrada;
    private Map<String, Integer> projetos;
    private Map<String, Integer> versoes;
	
    public void onProjetoChange() {
        if(projetoEntrada !=null && !projetoEntrada.equals(""))
            versoes = data.get(projetoEntrada);
        else
            versoes = new HashMap<String, Integer>();
    }
    
	private void initFormEntrada() {
		//Depois, esta inicialização deverá ser automatizada, para pegar as informações diretamente dos cards do Trello
		
		projetos = new HashMap<String, Integer>();
		projetos.put("Custas Judiciais", 55);
		projetos.put("PROMAGIS", 12);
		projetos.put("CERTO", 128); 
		projetos.put("TJ Calc",167);
		projetos.put("ADM Eletrônico", 45);
		projetos.put("SJN Express", 169);
		
		//Custas
		Map<String, Integer> map =  new HashMap<String, Integer>();
		map.put("3.0", 1329);
		map.put("3.1", 1347);
		data.put(55, map);
		
		//PROMAGIS
		map =  new HashMap<String, Integer>();
		map.put("2.0_h1", 1333);
		map.put("2.0", 1056);
		data.put(12, map);
		
		//CERTO
		map =  new HashMap<String, Integer>();
		map.put("1.2", 1318);
		data.put(128, map);
		
		//TJ Calc
		map =  new HashMap<String, Integer>();
		map.put("1.0", 1337);
		data.put(167,map);
		
		//ADM Eletrônico
		map =  new HashMap<String, Integer>();
		map.put("2.1", 1356);
		data.put(45, map);
		
		//SJN Express
		map =  new HashMap<String, Integer>();
		map.put("1.0", 1354);
		data.put(169, map);
	}
	
	public void buscarTickets() {
		tickets = cliente.getTickets(versaoEntrada);
		calcularPesos();
	}
}
