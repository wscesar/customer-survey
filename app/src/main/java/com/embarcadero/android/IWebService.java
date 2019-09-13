
package com.embarcadero.android;

/**
 * Implementada por classes que fazem conexao direta com um webservice.
 * @author Vitor Bonequini
 * @param <T> O tipo do objeto de conexao.
 * @param <E> O tipo do objeto de acesso aos metodos.
 */
public interface IWebService<T, E>
{
	
	/** 
	 * @return Um objeto de conex�o com o banco de dados/webservice 
	 */
	T getConnection();
	
	/** 
	 * @return Um objeto de acesso a fun��es/m�todos do webservice 
	 */
	E getMethod();
}
