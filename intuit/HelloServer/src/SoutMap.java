import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.server.Request;

public class SoutMap {
	
	public void  printMap(Request baseRequest) {
	
	System.out.println("Начало printMap");
	
    Map<String, String[]> m=baseRequest.getParameterMap();
    @SuppressWarnings("rawtypes")
	Set s = m.entrySet();
    Iterator it = s.iterator();

        while(it.hasNext()){

            Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)it.next();

            String key             = entry.getKey();
            String[] value         = entry.getValue();

            System.out.println("Key is "+key);

                if(value.length>1){    
                    for (int i = 0; i < value.length; i++) {
                    	System.out.println(value[i].toString());
                    }
                }else
                	System.out.println("Value is "+value[0].toString());

                System.out.println("-------------------");
        }

   	System.out.println("Конец printMap");

	}

}
