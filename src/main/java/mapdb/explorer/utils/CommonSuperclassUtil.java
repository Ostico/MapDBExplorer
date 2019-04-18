package mapdb.explorer.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommonSuperclassUtil {

	
	// from:
	// http://stackoverflow.com/questions/9797212/finding-the-nearest-common-superclass-or-superinterface-of-a-collection-of-cla
	
	
	private static Set<Class<?>> getClassesBfs(Class<?> clazz) {
	    Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
	    Set<Class<?>> nextLevel = new LinkedHashSet<Class<?>>();
	    nextLevel.add(clazz);
	    do {
	        classes.addAll(nextLevel);
	        Set<Class<?>> thisLevel = new LinkedHashSet<Class<?>>(nextLevel);
	        nextLevel.clear();
	        for (Class<?> each : thisLevel) {
	            Class<?> superClass = each.getSuperclass();
	            if (superClass != null && superClass != Object.class) {
	                nextLevel.add(superClass);
	            }
	            for (Class<?> eachInt : each.getInterfaces()) {
	                nextLevel.add(eachInt);
	            }
	        }
	    } while (!nextLevel.isEmpty());
	    return classes;
	}

	public static List<Class<?>> commonSuperClass(Class<?>... classes) {
		if(classes.length == 0)  {
			List<Class<?>> l = new ArrayList<>();
			l.add(Object.class);
			return l;
		}
	    // start off with set from first hierarchy
	    Set<Class<?>> rollingIntersect = new LinkedHashSet<Class<?>>(
	            getClassesBfs(classes[0]));
	    // intersect with next
	    for (int i = 1; i < classes.length; i++) {
	        rollingIntersect.retainAll(getClassesBfs(classes[i]));
	    }
	    return new LinkedList<Class<?>>(rollingIntersect);
	}

}
