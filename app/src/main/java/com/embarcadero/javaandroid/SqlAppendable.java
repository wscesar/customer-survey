package com.embarcadero.javaandroid;

/**
 * Define metodos que auxiliam na construcao de comandos SQL.
 * @author Vitor Bonequini
 */
public interface SqlAppendable extends Appendable
{
	/**
	 * Adiciona uma aspa simples na String do comando.
	 * @return A propria instancia do SqlAppendable.
	 */
	SqlAppendable appendSingleQuote();
	
	/**
	 * Adiciona aspas duplas na String do comando.
	 * @return A propria instancia do SqlAppendable.
	 */
	SqlAppendable appendDoubleQuotes();
}
