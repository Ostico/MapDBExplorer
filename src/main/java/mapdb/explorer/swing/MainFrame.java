package mapdb.explorer.swing;

import java.beans.IntrospectionException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import mapdb.explorer.MapDbExplorer;
import mapdb.explorer.model.BeanTableModel;
import mapdb.explorer.utils.MapDecorator;

public class MainFrame extends GeneratedFrame {


	private static final long serialVersionUID = 4841325141571053126L;

	
	
	
	private void setTables(Map<String, TableModel> tables)
	{
		this.tables = tables;
		if(tables == null)
		{
			table.setModel( new DefaultTableModel() );
			list.setModel(new DefaultListModel<>());
		}
		else
		{
			List<String> tableNames = new ArrayList<>(tables.keySet());
			Collections.sort(tableNames);
			list.setModel(new AbstractListModel<String>() {
				
				private static final long serialVersionUID = -9211586097308108865L;
				public int getSize() {
					return tableNames.size();
				}
				public String getElementAt(int index) {
					return tableNames.get(index);
				}
			});
			
			table.setModel(tables.size() == 0 ? new DefaultTableModel() :  tables.get(tables.keySet().iterator().next()));
			
		}
	}

	
	/* VERSIONE 3.0.1
	*/
	private static Map<String,Map<?, ?>> getMaps(String jarFile, String dbFile) throws Exception  {
		
		// apro il classloader per caricare le classi che trovera' dentro il map
		URLClassLoader child = new URLClassLoader (new URL[]{new File(jarFile).toURI().toURL()}, MapDbExplorer.class.getClassLoader());

		// setto come current
		Thread.currentThread().setContextClassLoader(child);
	
		
		DB db = DBMaker.fileDB(dbFile).closeOnJvmShutdown()
				
				.make();
		
		
		Map<String,Map<?, ?>> res = new HashMap<>();
		
		for (Entry<String, Object> e : db.getAll().entrySet()) {
			// per ora mostro solo i map (ci sarebbero anche i set)
			if(e.getValue() instanceof Map)
			{
				Map<?,?> m = (Map<?, ?>) e.getValue();
				
				// questa servirebbe per committare le modifiche fatte nella tabella (la tabella non conosce MapDb ma vede solo Map)
				Map<?, ?> wrappedMap = aggiungiAutoCommitTraballante(db, m);
				
				res.put(e.getKey(), wrappedMap);
			}
		}
		return  res;
	}
	
	
	
	/* VERSIONE 1.0.8
	private static Map<String,Map<?, ?>> getMaps(String jarFile, String dbFile) throws Exception {
		
		// apro il classloader per caricare le classi che trovera' dentro il map
		URLClassLoader child = new URLClassLoader (new URL[]{new File(jarFile).toURI().toURL()}, MapDbExplorer.class.getClassLoader());

		// setto come current
		Thread.currentThread().setContextClassLoader(child);
		
			
		DB db = DBMaker.newFileDB(new File(dbFile))
		        .closeOnJvmShutdown()
		        .make();
		
		
		Map<String,Map<?, ?>> res = new HashMap<>();
		
		for (Entry<String, Object> e : db.getAll().entrySet()) {
			// per ora mostro solo i map (ci sarebbero anche i set
			if(e.getValue() instanceof Map)
			{
				Map<?,?> m = (Map<?, ?>) e.getValue();
				
				// questa servirebbe per committare le modifiche fatte nella tabella (la tabella non conosce MapDb ma vede solo Map)
				Map<?, ?> wrappedMap = aggiungiAutoCommitTraballante(db, m);
				
				res.put(e.getKey(), wrappedMap);
			}
		}
		return  res;
	}
	*/

	@SuppressWarnings("unchecked")
	private static Map<?, ?> aggiungiAutoCommitTraballante(DB db, Map<?, ?> m) {
		@SuppressWarnings("rawtypes")
		Map<?,?> wrappedMap = new MapDecorator(m){

			@Override
			public Object put(Object key, Object value) {
				
				Object r = super.put(key, value);
				db.commit();
				System.out.println("Committato");
				return r;
			}
			
			
			
		};
		return wrappedMap;
	}
	
	private static Map<String, TableModel> createTables(Map<String, Map<?, ?>> maps) throws IntrospectionException {
		
		
		return maps
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						e -> e.getKey(), 
						e -> new BeanTableModel(e.getValue())
					));
		
	
	}
	


	@Override
	protected void openDatabase() throws Exception {
		OpenDatabaseDialog d = new OpenDatabaseDialog();
		d.setVisible(true);
		if(d.ok)
		{
			String jar = d.getJarFileName().getText();
			String db = d.getDbFileName().getText();
			
			Map<String, Map<?, ?>> dataBaseMaps = getMaps(jar, db);
			
			Map<String, TableModel> tables = createTables(dataBaseMaps);
			
			setTables(tables);
		}
		
	}
	
}
