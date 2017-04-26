import java.io.*;
import java.net.*;
import java.util.*;

import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.*;

class FileServer {
	public HttpServer server;

	static void respond(HttpExchange t,String response){
		try {
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void respondWithFileContents(HttpExchange exchange,String filepath)throws Exception{
		byte[]f=CommonFuncs.readFile(filepath);
		exchange.sendResponseHeaders(200, f.length);
		OutputStream os = exchange.getResponseBody();
		os.write(f);
		os.close();
	}
	public FileServer() throws Exception {

		final int port = 80;
		final String sitepath=new String(CommonFuncs.readFile("root_dir.txt")); // website root path
		final boolean directoryIndexAllowed=true; // is directory listing allowed

		server = HttpServer.create(new InetSocketAddress(port), 0);
		HttpContext c;

		c = server.createContext("/", new HttpHandler(){ // bind root uri
			public void handle(final HttpExchange exchange){
				new ClientRespondThread(exchange,sitepath,directoryIndexAllowed).start();

			}
		});

		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public void stop(){
		this.server.stop(1);
		//this.server.shutdownNow();
	}

	public static void main(String[]args)throws Exception{
		try{
			new FileServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
class ClientRespondThread extends Thread{
	HttpExchange exchange;
	String sitepath;
	boolean directoryIndexAllowed;
	public ClientRespondThread(HttpExchange e,String SitePath,boolean dirindxallowed){
		exchange=e;
		sitepath=SitePath;
		directoryIndexAllowed=dirindxallowed;
	}
	public void run(){
				try{
					String requesturi=CommonFuncs.remSlashes(URLDecoder.decode(exchange.getRequestURI().toString()));
					System.out.println(requesturi);
					String finalpath =CommonFuncs.remSlashes(sitepath+requesturi);
					System.out.println(finalpath);

					String response = "";

					File folder = new File(finalpath);

					String indexhtmlpath=finalpath+"/"+"index.html";
					String indexphppath=finalpath+"/"+"index.php";
					File indexhtml=new File(indexhtmlpath);
					File indexphp=new File(indexphppath);

					boolean indexHtmlFileExists= (indexhtml.exists() && !indexhtml.isDirectory());
					boolean indexPhpFileExists=(indexphp.exists() && !indexphp.isDirectory());

					if(folder.exists()){
						if(folder.isFile()){ // if requested path is file, serve it
							respondWithFileContents(exchange,finalpath);
							return;
						}else{ // if requested path is directory,
							if(indexHtmlFileExists || indexPhpFileExists){
								String tmppath="";
								if(indexPhpFileExists){
									tmppath=indexphppath;
								}else if(indexHtmlFileExists){
									tmppath=indexhtmlpath;
								}else{
									System.out.println("wtf");
								}

								respondWithFileContents(exchange,tmppath);
								return;
							}else if(directoryIndexAllowed){ //list it only if directory indexing setting is true, ortherwise 403 forbidden
								response+="<h1>Index</h1><BR/>";
								for (final File fileEntry : folder.listFiles()) { // list folders in directory fiest
									if (fileEntry.isDirectory()) {
										String entryname=fileEntry.getName();
										String entrypath=CommonFuncs.remSlashes(URLEncoder.encode(requesturi+"/"+entryname));
										String entry="<a href='"+entrypath+"'>"+entryname+"</a><BR/>";
										//System.out.println(entry);
										response+=entry;
									}
								}

								for (final File fileEntry : folder.listFiles()) { // list files in directory second
									if (!fileEntry.isDirectory()) {
										String entryname=fileEntry.getName();
										String entrypath=CommonFuncs.remSlashes(URLEncoder.encode(requesturi+"/"+entryname));
										String entry="<a href='"+entrypath+"'>"+entryname+"</a><BR/>";
										//System.out.println(entry);
										response+=entry;
									}
								}
							}else{
								response="403";
							}
						}
					}else{
						response="404";
					}

					exchange.sendResponseHeaders(200, response.length());
					OutputStream os = exchange.getResponseBody();
					os.write(response.getBytes());
					os.close();

				}catch(Exception e){
					e.printStackTrace();
				}
	}
}
}


