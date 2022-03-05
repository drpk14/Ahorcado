package ahorcado;
 
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList; 
import java.util.Properties; 

public class Servidor {
    
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("configuracion.ini");
        Properties prop = new Properties();
        prop.load(is); 
        int portService = Integer.valueOf(prop.getProperty("puerto"));
        ServerSocket serverSocket = null;
        
        try {
            System.out.println("Servidor Escuchando");
            serverSocket = new ServerSocket(portService);
        } catch (IOException e) {
            System.err.println("No es posible escuchar por el puerto: " + portService);
            System.exit(1);
        }
        
        Socket clientSocket = null; 
        
        try {
            while (true) {
                clientSocket = serverSocket.accept();
                new ServidorHilo(clientSocket).start();
                System.out.println("Cliente conectado.");
            }
        } catch (IOException e) {
            System.err.println("Fallo al aceptar conexión."); 
        } 
    }
    
    public static class ServidorHilo extends Thread {
        
        private Socket clientSocket = null; 
        private Protocolo protocolo;
        private String palabra;
        private int numErrores;
        private int numAciertos;
        private ArrayList<Character> letrasUsadas = new ArrayList();
        public boolean seguir;

        public ServidorHilo(Socket nCliente) {
            clientSocket = nCliente;
            this.protocolo = new Protocolo(this);  
            seguir = true;
        }
        
        public String getPalabra() {
            return palabra;
        }

        public void setPalabra(String palabra) {
            this.palabra = palabra;
        } 

        public int getNumErrores() {
            return numErrores;
        }

        public void setNumErrores(int numErrores) {
            this.numErrores = numErrores;
        }

        public int getNumAciertos() {
            return numAciertos;
        }

        public void setNumAciertos(int numAciertos) {
            this.numAciertos = numAciertos;
        }

        
        public ArrayList<Character> getLetrasUsadas() {
            return letrasUsadas;
        }

        public void setLetrasUsadas(ArrayList<Character> letrasUsadas) {
            this.letrasUsadas = letrasUsadas;
        } 
        
        public boolean comprobarLetra(char letra){
            for(Character letraActual : letrasUsadas){
                if(letraActual == letra){
                    return true;
                }
            }
           
           return false;
       }

        public boolean isSeguir() {
            return seguir;
        }

        public void setSeguir(boolean seguir) {
            this.seguir = seguir;
        }
        
        
        
        public void run() {
            
            try {
                PrintWriter out = null;
                BufferedReader in = null;
                String entrada = "";
                String salida = "";
                
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                while (seguir) {
                    
                    entrada = in.readLine();
                    
                    System.out.println("El servidor ha recibido: " + entrada);
                    
                    
                    salida = protocolo.procesarEntrada(entrada);
                    out.println(salida); 
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
             
        }
        
       
    } 
}
