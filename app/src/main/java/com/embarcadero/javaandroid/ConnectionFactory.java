package com.embarcadero.javaandroid;

import com.embarcadero.javaandroid.DSRESTConnection;
import android.util.Log;

/**
 * Fornece objetos de conexao para todas outras classes que precisam de acesso ao webservice.
 * @author Vitor Bonequini
 */
public final class ConnectionFactory
{
	private static final String TAG = ConnectionFactory.class.getSimpleName();

	/** Conexao armazenada para nao ser necessario instanciar varias vezes. */
	private static DSRESTConnection sCachedConnection;

	/** Ip do servidor. */
	public static String sIp;

	/** Porta para o servidor. */
	public static int sPorta;

	/**
	 * @return Uma variavel de conexao com o <strong>webservice</strong>.
	 * @exception NullPointerException Caso os atributos necessarios nao tenham sido
	 * inicializados.
	 */
	public static final DSRESTConnection getConnection()
	{
		return getConnection(sIp, sPorta);
	}

	/**
	 * @return Uma variavel de conexao com o <strong>webservice</strong>.
	 * @exception NullPointerException Caso um dos argumentos seja nulo e
	 * nao exista conexao no cache.
	 */
	public static final DSRESTConnection getConnection(String ip, int port)
	{
		if(!isConexaoIgualParametros(sCachedConnection, ip, port))
		{
			sCachedConnection = new DSRESTConnection();
			sCachedConnection.setHost(ip);
			sCachedConnection.setPort(port);
			Log.i(TAG, "Conexao criada ip: "+ ip + " port: " + port);
		}
		return sCachedConnection;
	}

	/**
	 * Torna a instancia da conexao nula.
	 */
	public static final void closeConnection()
	{
		sCachedConnection = null;
	}

	/**
	 * Testa se a conexao passada como argumento possui os parametros
	 * tambem passados como argumentos.
	 */
	private static boolean isConexaoIgualParametros(DSRESTConnection conn, String host, int port)
	{
		if(conn == null) return false;
		else return (conn.getHost().equals(host) && conn.getPort() == port);
	}

	/**
	 * Testa se as conexoes passadas como argumento possuem o mesmo host e porta.
	 */
	public static final boolean equals(final DSRESTConnection ... connections)
	{
		String firstHost = connections[0].getHost();
		int firstPort = connections[0].getPort();
		for(DSRESTConnection conn : connections)
		{
			String host = conn.getHost();
			int port = conn.getPort();
			if(!host.equals(firstHost) || port != firstPort) return false;
		}
		return true;
	}

}
