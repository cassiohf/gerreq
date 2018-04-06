package br.jus.tjpb.requisitos.servico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.jus.tjpb.requisitos.entidade.AcaoCard;
import br.jus.tjpb.requisitos.entidade.CardTrello;

@ManagedBean(name="clienteTrello")
@SessionScoped
public class ClienteTrello {
	
	@ManagedProperty("#{clienteRedmine}")
	private ClienteRedmine cliente;

	public ClienteRedmine getCliente() {
		return cliente;
	}

	public void setCliente(ClienteRedmine cliente) {
		this.cliente = cliente;
	}

	public List<CardTrello> getCards(Object[] objects) {
		String api_key = "a2adfb6f5aeb7055d60384593bf0a276";
		String token_trello = "7600ff7f3f0592dedc6b2bf362defa24f44e57d46f2ba965919bf9a2b91ba7a0";
		
		String autorizacao="?key="+api_key+"&token="+token_trello;
		String criterio = "&cards=open&lists=open";

		String endereco;
		
		ArrayList<CardTrello> listaCards = new ArrayList<CardTrello>();
		
		for(int item=0;item < objects.length;item++) {//////////
			try {
				endereco = "https://api.trello.com/1/lists/"+objects[item]+"/cards"+autorizacao+criterio;
	
				Map<String, String> listas_ativas = new LinkedHashMap<String, String>();
				listas_ativas.put("56cf6e735694b3d802e8d8a0", "Elicitação de requisitos");
				listas_ativas.put("56cf6e8ef40ca6ca3bcbe26f", "Documentação");
				listas_ativas.put("56cf6e9ae769951207321a17", "Contagem APF detalhada");
				listas_ativas.put("598362c41c81f436c57b99d8", "Contagem APF Aplicação");
				
				Map<String, String> listas_inativas = new LinkedHashMap<String, String>();
				
				listas_inativas.put("56cf6e60d388d1bb6f709154",  "Versões planejadas");
				listas_inativas.put("573105b3fdf760380b56c518", "Impedimento");
				listas_inativas.put("56d053be1830ffb4bb00ab76", "Monitoramento Desenvolvimento/Testes/Homologação");
				listas_inativas.put("56d053c8e20afbe70cc0d25d", "Concluídos");
				
				URL url = new URL(endereco);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.tjpb.jus.br",3128));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
				conn.setRequestMethod("GET");
	
				conn.setRequestProperty("Accept", "application/json");
				
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed! HTTP error code: "+conn.getResponseCode());
				}
				
				BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line;
				StringBuffer output = new StringBuffer();
				
				while ((line=buffer.readLine()) != null) {
					output.append(line);
					
				}
				
				JSONArray arr = new JSONArray(output.toString());
				JSONObject obj, acaojson;
				CardTrello ct;
				
				String endAcoes;
				URL urlAcoes;
				HttpURLConnection connAcoes;
				JSONArray arrAcoes;
				AcaoCard acao;
				
				//Percorrer o arquivo JSON buscando os cards retornados do Trello
				for(int i=0;i<arr.length();i++){
					obj = arr.getJSONObject(i);
					ct = new CardTrello();
					
					//DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
					
					DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					
					ct.setId(obj.getString("id"));
					ct.setName(obj.getString("name"));
					ct.setShortLink(obj.getString("shortUrl"));
	
					if (!obj.isNull("due")) {
						ct.setDataEntrega(formatter.parseDateTime(obj.getString("due")));
					}
					
					ct.setCardDataFinalizada(obj.getBoolean("dueComplete"));
					ct.setDataUltimaAtividade(formatter.parseDateTime(obj.getString("dateLastActivity")));
					ct.setDescricao(obj.getString("desc"));
					
					String[] descricoes = ct.getDescricao().split("@");
					
					int indice;
					String chave, valor;
					
					//Obtendo as descrições constantes no campo textual
					for (int j = 0; j < descricoes.length; j++) {
						indice = descricoes[j].indexOf(" ");
						if(indice > 0) {
							chave = descricoes[j].substring(0, indice).trim();
							valor = descricoes[j].substring(indice, descricoes[j].length()).trim();
							ct.getDescricoes().put(chave, valor);
							if(chave.equals("APFEstimativa") && !valor.isEmpty()){
								ct.setApfEstimada(Integer.parseInt(valor));
							}else if(chave.equals("APFDetalhada") && !valor.isEmpty()) {
								ct.setApfDetalhada(Integer.parseInt(valor));
							}
						}
						
					}
					
					//Obter tickets Redmine da versão constante no Trello
					String versaoRedmine = ct.getDescricoes().get("VersaoNoRedmine");
					if (!versaoRedmine.isEmpty()) {
						int numVersao = Integer.parseInt(versaoRedmine);
						ct.setTicketsRedmine(cliente.getTickets(numVersao));
						ct.calcularPercentualTotal();
					}
					
					
					
					//membros do card
					JSONArray arrMembros = obj.getJSONArray("idMembers");
					for (int j = 0; j < arrMembros.length(); j++) {
						ct.addMembro(arrMembros.getString(j));
					}
					
					ct.setIdList(obj.getString("idList"));
					
					//https://api.trello.com/1/cards/5a54eb6bbc07f38df91af4e0/actions?key=a2adfb6f5aeb7055d60384593bf0a276&token=7600ff7f3f0592dedc6b2bf362defa24f44e57d46f2ba965919bf9a2b91ba7a0&cards=open&lists=open
					endAcoes = "https://api.trello.com/1/cards/"+ct.getId()+"/actions"+autorizacao+criterio;
					urlAcoes = new URL(endAcoes);
					connAcoes = (HttpURLConnection) urlAcoes.openConnection(proxy);
					connAcoes.setRequestMethod("GET");
					connAcoes.setRequestProperty("Accept", "application/json");
					
					if (connAcoes.getResponseCode() != 200) {
						throw new RuntimeException("Failed! HTTP error code: "+connAcoes.getResponseCode());
					}
					
					buffer = new BufferedReader(new InputStreamReader(connAcoes.getInputStream(), "UTF-8"));
					output.delete(0, output.length());
					
					while ((line=buffer.readLine()) != null) {
						output.append(line);
					}
					
					buffer.close();
					
					
					//Ações do card
					arrAcoes = new JSONArray(output.toString());
					
					int tempoAtivo = 0;
					int tempo;
					for(int j=arrAcoes.length()-1;j>=0;j--){
						tempo=0;
						acaojson = arrAcoes.getJSONObject(j);
						
						if(acaojson.getJSONObject("data").has("listBefore")) {
							acao = new AcaoCard();
							acao.setIdAcao(acaojson.getString("id"));
							acao.setIdCartao(ct.getId());
							acao.setIdCriador(acaojson.getString("idMemberCreator"));
							acao.setIdListaAntes(acaojson.getJSONObject("data").getJSONObject("listBefore").getString("id"));//
							acao.setListaAntes(acaojson.getJSONObject("data").getJSONObject("listBefore").getString("name"));//
							acao.setIdListaDepois(acaojson.getJSONObject("data").getJSONObject("listAfter").getString("id"));//
							acao.setListaDepois(acaojson.getJSONObject("data").getJSONObject("listAfter").getString("name"));//
							acao.setDataOcorrencia(formatter.parseDateTime(acaojson.getString("date")));
							ct.addAcao(acao);
							System.out.println(acao.toString());

							if (j<arrAcoes.length()-1 && ct.getListaAcoes().size()>1 && listas_ativas.containsKey(acao.getIdListaAntes())) {
								tempo += Days.daysBetween(ct.getListaAcoes().get(ct.getListaAcoes().size()-2).getDataOcorrencia(), acao.getDataOcorrencia()).getDays();
								System.out.println("Tempo decorrido em "+acao.getListaAntes()+": "+ tempo);
								tempoAtivo+=tempo;
							}
						}
					}
					if(!ct.getListaAcoes().isEmpty()) {
						tempo = Days.daysBetween(ct.getListaAcoes().get(ct.getListaAcoes().size()-1).getDataOcorrencia(), DateTime.now()).getDays();
						System.out.println("Tempo decorrido em "+ct.getListaAcoes().get(ct.getListaAcoes().size()-1).getListaDepois()+": "+tempo);
						tempoAtivo+=tempo;
					}
					ct.setTempoAtivo(tempoAtivo);
					listaCards.add(ct);
					
					System.out.println(ct.toString());
				}
	
				conn.disconnect();
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}//////////
		return listaCards;

	}

}
