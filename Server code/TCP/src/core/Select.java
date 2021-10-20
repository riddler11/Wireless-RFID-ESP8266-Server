package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.utils.JdbcUtils;

import tool.TL;

/**
	 * 
	 * @author ???
	 * @version 1.0
	 * @since  9.0.4
	 * 
	 * 
	 */
 class Selecti {

	public void seclet() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM rcard ";
			
		   
			Connection conn = null;
		    PreparedStatement st = null;
			ResultSet rs = null;

			conn = JdbcUtils.getConnection();
			ArrayList<TL> list=new ArrayList<TL>(); 
			try {
				st = conn.prepareStatement(sql);
				rs = st.executeQuery();
				while (rs.next()) {
					TL toolg=new TL();
					toolg.setUser_name(rs.getString("user_name"));
					
					toolg.setDatetime(rs.getString("datetime"));
                    list.add(toolg);
			       }
				  
				} catch (Exception e) {
				 
			}   
			 Gson gson=new  Gson(); 
    	     String str=gson.toJson(list);
    	    System.out.println(str);
     } 
	}
