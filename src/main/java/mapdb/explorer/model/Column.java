package mapdb.explorer.model;

import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class Column {
	public String name;
	
	public Class cls;
	public Function getValue;
	public boolean editable;
	public BiConsumer setValue;
	
	public Column(String name, Class cls, Function getValue, boolean editable, BiConsumer setValue) {
		super();
		this.name = name;
		this.cls = cls;
		this.getValue = getValue;
		this.editable = editable;
		this.setValue = setValue;
	}
	
	
	
}
