package com.paypal.ipn.sample;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.paypal.ipn.IPNHandler;
import com.paypal.ipn.google.ac2dm.AndroidCloud2DeviceMessaging;

/**
 * Manipulador de notificação instantânea de pagamento que envia a mensagem para
 * dispositivos Android.
 * 
 * @author João Batista Neto
 */
public class IPNToAC2DMHandler implements IPNHandler {
	private SampleModel model;

	public IPNToAC2DMHandler(SampleModel model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.paypal.ipn.IPNHandler#handle()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void handle(Boolean isVerified, HttpServletRequest message) {
		if (isVerified) {
			AndroidCloud2DeviceMessaging ac2dm = new AndroidCloud2DeviceMessaging();
			Enumeration fields = message.getParameterNames();

			while (fields.hasMoreElements()) {
				String field = (String) fields.nextElement();

				ac2dm.addData(field, message.getParameter(field), "ipn");
			}

			ac2dm.send(model.getRegistrationId(message
					.getParameter("receiver_email")), model.getAuth());
		}
	}
}