package com.paypal.ipn.sample;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.paypal.ipn.InstantPaymentNotification;

/**
 * Controlador que receberá as notificações IPN e utilizará a classe
 * {@link InstantPaymentNotification} para validar e despachar as notificações
 * para dispositivos Android.
 */
public class IPNToAC2DMServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IPNToAC2DMServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		InstantPaymentNotification ipn = new InstantPaymentNotification();
		ipn.setIPNHandler(new IPNToAC2DMHandler(new SampleModel()));
		ipn.listen(request);
	}
}