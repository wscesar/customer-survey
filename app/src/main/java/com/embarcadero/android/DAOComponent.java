
package com.embarcadero.android;

import com.bbi.pesquisa.model.NetworkConfiguration;
import com.embarcadero.android.DSProxy.TSvrMethod;


/**
 * Representa um componente de conexao com a base de dados/webservice.
 * @author Vitor Bonequini
 */
public class DAOComponent implements IWebService<DSRESTConnection, TSvrMethod>
{
	/** Variavel contendo a conexao com o webservice. */
	private DSRESTConnection mConnection;
	
	/** Variavel de acesso a <strong>funcoes/metodos</strong> do webservice. */
	private TSvrMethod mMethodAcess;
	
	
	public DAOComponent()
	{
		mConnection = ConnectionFactory.getConnection();
		mMethodAcess = new TSvrMethod(mConnection);
	}

	public DAOComponent(NetworkConfiguration network)
	{
		mConnection = ConnectionFactory.getConnection(network.getIp(), network.getPort());
		mMethodAcess = new TSvrMethod(mConnection);
	}

	public DAOComponent(String ip, int port)
	{
//		ConnectionFactory.mConn(ip, port);
		mConnection = ConnectionFactory.getConnection();
		mMethodAcess = new TSvrMethod(mConnection);
	}



	public DSRESTConnection getConnection()
	{
		return mConnection;
	}

	public TSvrMethod getMethod()
	{
		return mMethodAcess;
	}

}
