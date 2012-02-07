package com.paypal.ipn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class InstantPaymentNotification {
	private static final String HOST = "https://www.paypal.com";
	private static final String SANDBOX_HOST = "https://www.sandbox.paypal.com";

	private String endpoint = InstantPaymentNotification.HOST;
	private IPNHandler ipnHandler;

	public InstantPaymentNotification() {
		this(false);
	}

	/**
	 * Constroi o observador no notificação instantânea de pagamento informando
	 * o ambiente que será utilizado para validação.
	 * 
	 * @param sandbox
	 *            {@link Boolean} Define se será utilizado o ambiente de
	 *            produção ou o Sandbox.
	 */
	public InstantPaymentNotification(Boolean sandbox) {
		if (sandbox) {
			endpoint = InstantPaymentNotification.SANDBOX_HOST;
		}

		endpoint += "/cgi-bin/webscr?cmd=_notify-validate";
	}

	/**
	 * Aguarda por notificações de pagamento instantânea; Caso uma nova
	 * notificação seja recebida, faz a verificação e notifica um manipulador
	 * com o status (verificada ou não) e a mensagem recebida.
	 * 
	 * @param post
	 *            {@link HttpServletRequest} Dados postatos pelo PayPal.
	 * @see {@link InstantPaymentNotification#setIPNHandler()}
	 */
	public void listen(HttpServletRequest post) {
		if (ipnHandler != null && post.getParameter("receiver_email") != null) {
			try {
				BufferedReader reader = post.getReader();
				StringBuffer sb = new StringBuffer();
				String data;

				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}

				URL url = new URL(endpoint);
				URLConnection conn = url.openConnection();

				conn.setDoOutput(true);

				OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream());

				writer.write(sb.toString());
				writer.flush();
				writer.close();

				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				reader = new BufferedReader(in);
				sb = new StringBuffer();

				data = null;

				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}

				ipnHandler.handle(sb.toString().equals("VERIFIED"), post);
			} catch (IOException e) {
				Logger.getLogger(InstantPaymentNotification.class.getName())
						.log(Level.SEVERE, null, e);
			}
		}
	}

	/**
	 * Define o objeto que irá manipular as notificações de pagamento
	 * instantâneas enviadas pelo PayPal.
	 * 
	 * @param ipnHandler
	 *            {@link IPNHandler}
	 */
	public void setIPNHandler(IPNHandler ipnHandler) {
		this.ipnHandler = ipnHandler;
	}
}