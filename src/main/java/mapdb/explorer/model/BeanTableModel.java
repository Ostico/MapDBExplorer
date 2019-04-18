package mapdb.explorer.model;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import mapdb.explorer.utils.CommonSuperclassUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanTableModel implements TableModel {


	Map map;
	List keys; // salvo le chiavi per accesso indicizzato
	List<Column> columns = new ArrayList<>();
	
	
	
	public BeanTableModel(Map map) {
		
				
		try {
			this.map = map;
			
			// analizzo keys
			Class[] allKeyClasses = (Class[]) map.keySet().stream().map( k -> k.getClass()).distinct().toArray(n -> new Class[n]); 
			Class keyClass = CommonSuperclassUtil.commonSuperClass(allKeyClasses).get(0);
			
			// analizzo values
			Class[] allValueClasses = (Class[]) map.values().stream().map( v -> v.getClass()).distinct().toArray(n -> new Class[n]); 
			Class valueClass = CommonSuperclassUtil.commonSuperClass(allValueClasses).get(0);

			System.out.println("Detected map classes: "+keyClass+" -> "+ valueClass);
			
			keys = new ArrayList<>(map.keySet());
			
			// colonne per le chiavi
			aggiungiColonneChiavi(columns, keyClass);

			// colonne per i valori
			
			aggiungiColonneValori(columns, map, valueClass);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		
	}


	private void aggiungiColonneValori(List<Column> columns, Map map, Class valueClass) throws IntrospectionException {
		
		
		columns.add(new Column("Value Class", String.class, k -> map.get(k).getClass().getName(), false, null));
		
		// tipi base, anche editabili
		if(valueClass.equals(String.class))
		{
			columns.add(new Column("Value", valueClass, k -> map.get(k), true, (k,v) -> {
				map.put(k, v);
			}));
		}
		else if(valueClass.equals(Integer.class))
		{
			columns.add(new Column("Value", valueClass, k -> map.get(k), true, (k,v) -> {
				map.put(k, v);
			}));
		}
		else
		{
		
			// tipo non base, aggiungo l'oggetto ma anche le proprieta
			columns.add(new Column("Value", valueClass, k -> map.get(k), false, null));
		

			for ( PropertyDescriptor pd : Introspector.getBeanInfo(valueClass).getPropertyDescriptors() )
			{
				if(!pd.getName().equals("class"))
				{
					columns.add(new Column(pd.getName(), pd.getPropertyType(), k -> read(pd, map.get(k)), false, null ));
				}
			}
		}
	}


	private void aggiungiColonneChiavi(List<Column> columns, Class keyClass) throws IntrospectionException {
		columns.add(new Column("Key Class", String.class, k -> k.getClass().getName(), false, null));
		
		if(keyClass.equals(String.class))
		{
			columns.add(new Column("Key", keyClass, k -> k, false, null));
		}
		else if(keyClass.equals(Integer.class))
		{
			columns.add(new Column("Key", keyClass, k -> k, false, null));
		}
		else
		{
			// classe specifica, aggiungo il valore ma anche tutte le proprieta
			columns.add(new Column("Key", keyClass, k -> k, false, null));
			
			
			for ( PropertyDescriptor pd : Introspector.getBeanInfo(keyClass).getPropertyDescriptors() )
			{
				if(!pd.getName().equals("class"))
				{
					columns.add(new Column(pd.getName(), pd.getPropertyType(), k -> read(pd, k) , false, null));
				}
			}
			}
	}

	
	private static Object read(PropertyDescriptor pd, Object o)
	{
		try {
			return pd.getReadMethod().invoke(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	private static void write(PropertyDescriptor pd, Object o, Object v)
	{
		try {
			pd.getWriteMethod().invoke(o, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	*/
	@Override
	public int getRowCount() {
		
		return map.size();
	}

	@Override
	public int getColumnCount() {
	
		return columns.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columns.get(columnIndex).name;
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columns.get(columnIndex).cls;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		return columns.get(columnIndex).editable;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return columns.get(columnIndex).getValue.apply(keys.get(rowIndex));
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		columns.get(columnIndex).setValue.accept(keys.get(rowIndex), aValue);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
		
	}

}
