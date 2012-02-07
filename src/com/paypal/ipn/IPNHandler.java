package com.paypal.ipn;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface para definição de um manipulador de notificação de pagamento
 * instantânea.
 * 
 * @author João Batista Neto
 */
public interface IPNHandler {
	/**
	 * Manipula uma notificação de pagamento instantânea recebida pelo PayPal.
	 * 
	 * @param isVerified
	 *            {@link Boolean} Identifica que a mensagem foi verificada como
	 *            tendo sido enviada pelo PayPal.
	 * @param message
	 *            {@link HttpServletRequest} Mensagem completa enviada pelo
	 *            PayPal.
	 */
	public void handle(Boolean isVerified, HttpServletRequest message);
}