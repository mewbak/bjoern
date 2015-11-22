package server.commands.shellcreate;

import java.io.IOException;

import org.codehaus.groovy.tools.shell.Groovysh;

import server.commands.Constants;
import server.fileWalker.OrderedWalker;
import server.fileWalker.SourceFileWalker;

import com.tinkerpop.gremlin.Imports;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.gremlin.groovy.console.NullResultHookClosure;

public class BjoernGremlinShell
{
	Groovysh groovysh = new Groovysh();

	public BjoernGremlinShell()
	{
		silenceShell();
		performInitialImports();
		Gremlin.load();
		loadQueryLibrary();
		openDatabaseConnection();
	}

	private void silenceShell()
	{
		groovysh.setResultHook(new NullResultHookClosure(groovysh));
	}

	private void performInitialImports()
	{

		for (String imps : Imports.getImports())
		{
			groovysh.execute("import " + imps);
		}
		groovysh.execute("import com.tinkerpop.gremlin.Tokens.T");
		groovysh.execute("import com.tinkerpop.gremlin.groovy.*");
		groovysh.execute("import groovy.grape.Grape");
	}

	private void loadQueryLibrary()
	{
		try
		{
			loadRecursively(Constants.QUERY_LIB_DIR);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void loadRecursively(String queryLibDir) throws IOException
	{
		SourceFileWalker walker = new OrderedWalker();
		GroovyFileLoader listener = new GroovyFileLoader();
		listener.setGroovyShell(groovysh);

		walker.setFilenameFilter("*.groovy");
		walker.addListener(listener);
		walker.walk(new String[] { queryLibDir });
	}

	private void openDatabaseConnection()
	{
		String cmd = String.format("g = new OrientGraphNoTx(\"%s\");",
				Constants.PLOCAL_PATH_TO_DB);
		groovysh.execute(cmd);
	}

	public Object execute(String line)
	{
		try
		{
			return groovysh.execute(line);
		}
		catch (Exception ex)
		{
			return ex.getMessage();
		}

	}
}