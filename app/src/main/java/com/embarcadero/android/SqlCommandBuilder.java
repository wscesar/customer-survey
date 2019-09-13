package com.embarcadero.android;

// import com.embarcadero.javaandroid.TJSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Representa um construtor de Strings que auxilia a criacao de
 * comandos SQL.
 * @author Vitor Bonequini
 */
public class SqlCommandBuilder implements CharSequence, SqlAppendable
{
	/** Representa a capacidade inicial do StringBuilder interno. */
	private static final int INITIAL_CAPACITY = 100;
	
	@Expose @SerializedName("command")
	/** Representa o comando SQL final a ser enviado. */
	private String mCommand;
	
	/** Representa o comando SQL sendo construido. */
	private StringBuilder mPartialCommand;
	

	private SqlCommandBuilder(int size)
	{
		mPartialCommand = new StringBuilder(size);
	}
	
	private SqlCommandBuilder(CharSequence command)
	{
		mPartialCommand = new StringBuilder(command);
	}
	
	/**
	 * Cria uma nova instancia de um SqlCommandBuilder, iniciando o 
	 * StringBuilder com um array de {@value #INITIAL_CAPACITY} caracteres.
	 * @return A nova instancia criada.
	 */
	public static SqlCommandBuilder create()
	{
		return new SqlCommandBuilder(INITIAL_CAPACITY);
	}
	
	/**
	 * Cria uma nova instancia de um SqlCommandBuilder, iniciando o 
	 * StringBuilder com um array com o tamanho igual ao numero
	 * passado como parametro.
	 * @param O Tamanho incial do array de caracteres.
	 * @return A nova instancia criada.
	 */
	public static SqlCommandBuilder create(int size)
	{
		return new SqlCommandBuilder(size);
	}
	
	/**
	 * Cria uma nova instancia de um SqlCommandBuilder, iniciando o 
	 * StringBuilder com o CharSequence passado como parametro.
	 * @param command A sequencia de caracteres inciais.
	 * @return A nova instancia criada.
	 */
	public static SqlCommandBuilder create(CharSequence command)
	{
		return new SqlCommandBuilder(command);
	}

	@Override
	public int length()
	{
		return mPartialCommand.length();
	}

	@Override
	public char charAt(int index)
	{
		return mPartialCommand.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return mPartialCommand.subSequence(start, end);
	}
	
	public SqlAppendable appendSingleQuote()
	{
		return append('\'');
	}
	
	public SqlAppendable appendDoubleQuotes()
	{
		return append('\"');
	}
	
	@Override
	public SqlAppendable append(final char c)
	{
		mPartialCommand.append(c);
		return this;
	}
	
	@Override
	public SqlAppendable append(final CharSequence csq)
	{
		mPartialCommand.append(csq);
		return this;
	}

	@Override
	public SqlAppendable append(final CharSequence csq, int start, int end)
	{
		mPartialCommand.append(csq, start, end);
		return this;
	}
	
	/**
	 * Limpa o conteudo do comando SQL;
	 * @return A propria instancia.
	 */
	public SqlCommandBuilder clear()
	{
		mPartialCommand.setLength(0);
		return this;
	}
	
	/**
	 * Converte essa instancia em um objeto TJSON pronto para
	 * ser enviado ao Webservice.
	 * @return O Objeto TJSON.
	 */
	public TJSONObject toJson()
	{
		mCommand = mPartialCommand.toString();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(this);
		return TJSONObject.Parse(json);
	}
	
	@Override
	public String toString()
	{
		return mPartialCommand.toString();
	}

}
