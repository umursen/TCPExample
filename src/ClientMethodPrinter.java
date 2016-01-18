import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class ClientMethodPrinter {

	public static void PrintSyncCheck(String jSONArrayString){

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jSONArrayString);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(jsonArray.length() == 2){
			System.out.println("No update required!");
			return;
		}
			
		
		ArrayList<String> list = new ArrayList<String>();     

		if (jsonArray != null) { 
			int len = jsonArray.length();
			for (int i=0;i<len;i++){ 
				try {
					list.add(jsonArray.get(i).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(String s : list)
			System.out.println(s);
	}
}
