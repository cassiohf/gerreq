package br.jus.tjpb.requisitos.servico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.jus.tjpb.requisitos.entidade.TicketRedmine;

@ManagedBean(name="clienteRedmine")
@SessionScoped
public class ClienteRedmine {

	public List<TicketRedmine> getTickets(Integer numVersao) {
		
		//Promagis: 12; versão 1056
		//Certo: 128; versão: 1318
		//Custas: 55; versão: 1329
		//http://redmine.tjpb.jus.br/issues.json?project_id=55&fixed_version_id=1329
		//String endereco = "http://redmine.tjpb.jus.br/issues.json?project_id="+numProjeto+"&fixed_version_id="+numVersao;
		String endereco = "http://redmine.tjpb.jus.br/issues.json?&fixed_version_id="+numVersao;
		
		ArrayList<TicketRedmine> listaTickets = new ArrayList<TicketRedmine>();
		
		try {
			URL url  = new URL(endereco);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("X-Redmine-API-Key","f686f32946cff6059a75f8387696f34a34fddf6b");
			//'Content-type: application/json;charset=utf-8'
			
			if(conn.getResponseCode() != 200){
				throw new RuntimeException("Falha! HTTP Error code: "+conn.getResponseCode());
			}
			
			BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String saida;
			
			StringBuffer output = new StringBuffer();
			
			while((saida = buffer.readLine()) != null){
				output.append(saida);
			}
			
			JSONObject obj = new JSONObject(output.toString());
			JSONArray tickets = obj.getJSONArray("issues");
			
			JSONObject it;
			JSONArray customFields;
			br.jus.tjpb.requisitos.entidade.TicketRedmine ticket;
			
//			double esforcoTotal=0, esforcoRealizado=0;
			
			for (int i = 0; i < tickets.length(); i++) {
				it = tickets.getJSONObject(i);
				ticket = new TicketRedmine();
				
				ticket.setTicket(it.getInt("id"));
				
				ticket.setTitulo(it.getString("subject"));
				
				ticket.setIdProjeto(it.getJSONObject("project").getInt("id"));
				ticket.setNomeProjeto(it.getJSONObject("project").getString("name"));
				
				ticket.setIdTipoTicket(it.getJSONObject("tracker").getInt("id"));
				ticket.setTipoTicket(it.getJSONObject("tracker").getString("name"));
				
				ticket.setIdVersao(it.getJSONObject("fixed_version").getInt("id"));
				ticket.setVersao(it.getJSONObject("fixed_version").getString("name"));
				
				ticket.setPercTerminado(it.getInt("done_ratio"));

				if(it.has("custom_fields")) {
					customFields = it.getJSONArray("custom_fields");
					
					for(int j=0;j<customFields.length();j++){
						//se o custom field for "Esforço Análise"
						if(customFields.getJSONObject(j).getInt("id") == 28){
							ticket.setEsfocoAnalise(customFields.getJSONObject(j).getString("value").equals("")?0:
								Integer.parseInt(customFields.getJSONObject(j).getString("value"))
							);
						}
					}
				}else {
					ticket.setEsfocoAnalise(0);
				}

				if(ticket.getIdTipoTicket()==2 || ticket.getIdTipoTicket()==5) {
					
					listaTickets.add(ticket);
					
//					esforcoTotal += ticket.getEsfocoAnalise();
//					if(ticket.getPercTerminado() > 30) {
//						esforcoRealizado += ticket.getEsfocoAnalise();
//					}
					System.out.println(ticket.toString());
				}
				
				
			}

			conn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listaTickets;
	}

}
