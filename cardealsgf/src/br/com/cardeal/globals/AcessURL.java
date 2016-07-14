package br.com.cardeal.globals;
 
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import br.com.cardeal.log.LogDeProcessamento;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
 
public class AcessURL 
{
 
	URL url;
	URLConnection urlConnection;
	String urlName;
	
	public static void main(String... args){
		
		AcessURL url = new AcessURL("http://192.168.1.58:8081/cardealsgf/resources/labels/EtiquetaCaixa.txt");
		try {
			url.copyFileByURL("c:\\temp\\teste.txt");
		} catch (IOException e) {
			
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}
		
	}		
	
	public AcessURL( String urlName ){
//		   urlName = "http://192.168.1.58:8081/cardealsgf/resources/labels/EtiquetaCaixa.txt";		
		try {
			url = new URL(urlName);
			urlConnection = (URLConnection) url.openConnection();
			this.urlName = urlName;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		} catch (IOException e) {
			e.printStackTrace();
			LogDeProcessamento.gravaLog( "console", e.getClass().getName() + "\n" + e.getMessage(), true);
		}			
		
	}
   
   public void readFileTxtByURL(){
	   	   
		try {
		 
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			         
			String line = null;
			 
			while( (line = in.readLine()) != null ){
				System.out.println(line);
			}
		 
			in.close();         
		 
		} catch (MalformedURLException e){
			System.out.println("Erro ao criar URL. Formato inválido.");			
		} catch (IOException e2) {
			System.out.println("Erro ao acessar URL.");			
		}
	   
   }
   
   public void copyFileByURL(String destinationFile) throws IOException {
	   
			URL url = new URL(this.urlName);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
	   
  }
 
}