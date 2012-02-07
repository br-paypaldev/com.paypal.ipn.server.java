package com.paypal.ipn.google.ac2dm;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * A classe AndroidCloud2DeviceMessaging faz integração com o serviço Android
 * Cloud to Device Messaging (C2DM) para enviar dados para dispositivos Android.
 * 
 * @author João Batista Neto
 */
public class AndroidCloud2DeviceMessaging {
	/**
	 * O certificado do Google não cobre android.apis.google.com, então será
	 * preciso verificar e fazer a validação do certificado manualmente.
	 */
	private static final String HOSTNAME = "android.apis.google.com";

	/**
	 * URL do serviço C2DM.
	 */
	public static final String URL = "https://android.apis.google.com/c2dm/send";

	/**
	 * Conjunto de pares key=value que serão enviados para o dispositivo.
	 */
	private Map<String, Map<String, String>> data;

	public AndroidCloud2DeviceMessaging() {
		data = new HashMap<String, Map<String, String>>();
	}

	/**
	 * Adiciona um par key=value que será enviado para o dispositivo android.
	 * 
	 * @param key
	 *            {@link String} A chave que será enviada para o dispositivo.
	 * @param value
	 *            {@link String} O valor da chave.
	 * @param collapseKey
	 *            {@link String} Chave de agrupamento que será utilizado pelo
	 *            Google para evitar que várias mensagens do mesmo tipo sejam
	 *            enviadas para o usuário de uma vez quando o dispositivo fique
	 *            online.
	 */
	public void addData(String key, String value, String collapseKey) {
		Map<String, String> map;

		if ((map = data.get(collapseKey)) == null) {
			map = new HashMap<String, String>();

			data.put(collapseKey, map);
		}

		map.put(key, value);
	}

	/**
	 * Remove todas os pares key=value.
	 */
	public void clear() {
		data.clear();
	}

	/**
	 * Envia a mensagem para o servidor C2DM.
	 * 
	 * @param registrationId
	 *            {@link String} ID de registro do dispositivo android.
	 * @param auth
	 *            {@link String} Token de autorização.
	 * @see {@link com.paypal.ipn.google.auth.ClientLogin#getAuth}
	 */
	public void send(String registrationId, String auth) {
		try {
			URL url = new URL(URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return hostname.equals(HOSTNAME);
				}
			});
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth);

			for (String collapseKey : data.keySet()) {
				StringBuilder sb = new StringBuilder();
				Map<String, String> nv = data.get(collapseKey);

				sb.append("registration_id=" + registrationId);
				sb.append("&collapse_key=" + collapseKey);

				for (Entry<String, String> entry : nv.entrySet()) {
					sb.append("&data." + entry.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				}

				OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream());

				writer.write(sb.toString());
				writer.flush();
				writer.close();

				if (conn.getResponseCode() != 200) {
					Logger.getLogger(
							AndroidCloud2DeviceMessaging.class.getName()).log(
							Level.SEVERE, conn.getResponseMessage());
				}
			}
		} catch (IOException e) {
			Logger.getLogger(AndroidCloud2DeviceMessaging.class.getName()).log(
					Level.SEVERE, null, e);
		}
	}
}