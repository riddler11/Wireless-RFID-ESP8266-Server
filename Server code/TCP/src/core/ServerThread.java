package core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.utils.JdbcUtils;

/**
 * 
 * @author 燊哥仔
 * @version 1.0
 * @since  9.0.4
 * 
 * 
 */
/*
* 服务器线程处理类
*/
public class ServerThread extends Thread {
   // 和本线程相关的Socket
   Socket socket = null;

   public ServerThread(Socket socket) {
      this.socket = socket;
  
   }

	Map <String,Socket> map=new HashMap<>();
	
   //线程执行的操作，响应客户端的请求
   public void run(){
      InputStream is=null;
      InputStreamReader isr=null;
      BufferedReader br=null;
      OutputStream os=null;
      PrintWriter pw=null;
      try {
         //获取输入流，并读取客户端信息
         is = socket.getInputStream();
         isr = new InputStreamReader(is);
         br = new BufferedReader(isr);
         String info=null;

         //获取输出流，响应客户端的请求
         os = socket.getOutputStream();
         pw = new PrintWriter(os);

         while((info=br.readLine())!=null){//循环读取客户端的信息
            System.out.println("客户端："+info);          
            String[]  strs=info.split("&");
            System.out.println(strs[0].toString());
            System.out.println(strs[1].toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Connection conn = null;
			PreparedStatement st = null;
			 System.out.println(sdf.format(new Date()));
					try {
						String sql = "insert into rpass (rpass.user_name, rpass.ID,rpass.datetime ) values (?,?,?)";
						conn = JdbcUtils.getConnection();
						conn.setAutoCommit(true);
						st = conn.prepareStatement(sql);
						st.setString(1,strs[0].toString() );
						st.setString(2,strs[1].toString() );
						st.setString(3,sdf.format(new Date()));
						st.executeUpdate();
					} catch (SQLException e) {
						try {
							if (conn != null) {
								conn.rollback();
							}
						} catch (SQLException e1) {
							// throw new RuntimeException(e);
						}

					} finally {
						JdbcUtils.close(conn, st, null);
					}
            //Scanner sc=new Scanner(System.in);
            System.out.println("服务器反馈：收到\r");
            //String s1=sc.nextLine();
            //pw.write(s1);
            pw.flush();//调用flush()方法将缓冲输出
         }
         socket.shutdownInput();//关闭输入流
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }finally{
         //关闭资源
         try {
            if(pw!=null)
               pw.close();
            if(os!=null)
               os.close();
            if(br!=null)
               br.close();
            if(isr!=null)
               isr.close();
            if(is!=null)
               is.close();
            if(socket!=null)
               socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}