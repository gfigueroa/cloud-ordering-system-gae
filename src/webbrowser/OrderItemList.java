package webbrowser;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import java.util.Enumeration;

@SuppressWarnings("serial")

public class OrderItemList extends HttpServlet {
	
	
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		List<String> orderItemList = new ArrayList<String>();
		List<List<String>> CarSet = new ArrayList<List<String>>();
		
		PrintWriter out = response.getWriter();
		String Parameter=null;
		String ParameterValue=null;
		int Flag = 0;
		HttpSession session = request.getSession(true);
		Enumeration<?> ItemName = request.getParameterNames();
		if(session.getAttribute("CarSet") !=null)
		{
			CarSet = (List<List<String>>)session.getAttribute("CarSet");
		}
		if(CarSet.isEmpty()){
			out.println("it's empty!");
			while(ItemName.hasMoreElements())
			{
				Parameter = ItemName.nextElement().toString();
				ParameterValue = request.getParameter(Parameter);
				if(ParameterValue != "0"){
					orderItemList.add(Parameter);
					orderItemList.add(ParameterValue);
					CarSet.add(orderItemList);
				
					orderItemList = new ArrayList<String>();
				}

				
			}
		}else{
			out.println("it's not empty!");
			while(ItemName.hasMoreElements())
			{
				Parameter = ItemName.nextElement().toString();
				ParameterValue = request.getParameter(Parameter);
			
				for(List<String> Temp : CarSet){
					//out.println(Parameter);
					//out.println("/n/r");
					//out.println(Temp.get(0));
					//out.println(Parameter);
					out.println("check the data.");
					//out.println(Temp.get(0));
					if (Parameter.equals(Temp.get(0))){
						Flag = 1;
						//String ValueString = "+1";
						//Temp.set(1, ValueString);
						out.println("i got the same!");
					}else{
						out.println("it didn't find!");
					}
				
				}
				if(Flag!=1)
				{
					orderItemList.add(Parameter);
					orderItemList.add(ParameterValue);
					CarSet.add(orderItemList);
					orderItemList = new ArrayList<String>();
					Flag=0;
				}
				
			}
			
/*			for(List<String> CarSets : CarSet)
			{
				for(List<String> CarSetTemps :CarSetTemp)
				{
					out.println("check the data.");
					//out.println(CarSets.get(1));
					//out.println(CarSetTemps.get(1));
					String a = CarSets.get(0);
					String b = CarSetTemps.get(0);
					out.println(a);
					out.println(b);
					if(a.equals(b))
					{
						out.println("i got the same!");
						CarSets.set(1,CarSets.get(1)+"+1" );
					}
				}
			}*/
			
		}
		session.setAttribute("CarSet", CarSet);		
			

			
		
		
		
		//session.setAttribute("CarSet", CarSet);
		
		for(List<String> show : CarSet){
			out.print(show.get(0).toString());
			out.print("=");
			out.print(show.get(1).toString());
			out.print("\r\n");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<List<String>> getCar(HttpSession session){
		return (List<List<String>>) session.getAttribute("CarSet"); 
	}

	public static int ClearSession(HttpSession session){
		session.removeAttribute("CarSet");
		return 0;
	}
}	
