package com.martin.model.exceptions;

@SuppressWarnings("serial")
public class MoneyException extends Exception {

	public MoneyException() {
		super("L'action que vous essayer de r�aliser n'a pas aboutie : "
				+ "L'argent total serai pass� sous la barre "
				+ "des 0 �, et le jeu serai...bloqu�.");
	}

	public MoneyException(String string) {
		super(string);
	}

}
