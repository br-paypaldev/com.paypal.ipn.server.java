package com.paypal.ipn.google.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Faz a requisição POST ao ClientLogin do Google para obter a autorização de
 * acesso para contas Google.
 * 
 * @author João Batista Neto
 */
public class ClientLogin {
	/**
	 * URL do serviço de autorização ClientLogin do Google.
	 */
	public static final String URL = "https://www.google.com/accounts/ClientLogin";

	/**
	 * Token de autorização.
	 */
	private String auth;

	/**
	 * Obtém o token de autorização do Google utilizando ClientLogin
	 * 
	 * @param accountType
	 *            {@link String} Tipo da conta que está solicitando a
	 *            autorização, os valores possíveis são:
	 *            <ul>
	 *            <li>GOOGLE</li>
	 *            <li>HOSTED</li>
	 *            <li>HOSTED_OR_GOOGLE</li>
	 *            </ul>
	 * @param Email
	 *            {@link String} Email completo do usuário, incluindo o domínio.
	 * @param Passwd
	 *            {@link String} Senha do usuário.
	 * @param source
	 *            {@link String} Uma string identificando a aplicação.
	 * @param service
	 *            {@link String} Nome do serviço que será solicitada a
	 *            autorização.
	 * @return {@link String} O Token de autorização.
	 */
	public String getAuth(String accountType, String Email, String Passwd,
			String source, String service) {

		if (auth == null) {
			try {
				URL url = new URL(URL);
				URLConnection conn = url.openConnection();
				StringBuilder sb = new StringBuilder();

				sb.append("accountType=" + accountType);
				sb.append("&Email=" + Email);
				sb.append("&Passwd=" + Passwd);
				sb.append("&source=" + source);
				sb.append("&service=" + service);

				conn.setDoOutput(true);

				OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream());

				writer.write(sb.toString());
				writer.flush();
				writer.close();

				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				BufferedReader reader = new BufferedReader(in);
				sb = new StringBuilder();

				String data = null;

				while ((data = reader.readLine()) != null) {
					String nv[] = data.split("=");

					if (nv.length == 2 && nv[0].equals("Auth")) {
						auth = nv[0];
						break;
					}
				}

				reader.close();
			} catch (IOException e) {
				Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE,
						null, e);
			}
		}

		return auth;
	}
}