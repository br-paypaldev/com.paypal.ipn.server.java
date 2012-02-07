package com.paypal.ipn.sample;

import com.paypal.ipn.google.auth.ClientLogin;

public class SampleModel {
	public String getAuth() {
		String user = "usuario@gmail.com";
		String pswd = "senha";
		String type = "GOOGLE";
		
		//TODO: verificar se existe uma autorização e definir regras de negócio
		//para atualizá-la.

		ClientLogin cl = new ClientLogin();

		return cl.getAuth(type, user, pswd, "PayPalX-com.paypal.ipn-1.0",
				"ac2dm");
	}

	public String getRegistrationId(String email) {
		//TODO: adicionar obtenção do id de registro
		
		return null;
	}
}