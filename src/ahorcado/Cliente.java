package ahorcado;
  
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter; 
import java.net.Socket; 
import java.net.UnknownHostException;
import java.util.Properties; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    public static void main(String[] args) {
        BufferedReader teclado = new BufferedReader(new InputStreamReader (System.in));
        int opcion = 0; 
         
        Cliente c = new Cliente();
 
        InputStream is;
        Properties prop = new Properties();
        try {
            is = new FileInputStream("configuracion.ini");
            prop.load(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error IOException. " + ex);
        }

        String hostServerName = prop.getProperty("host");
        int servicePort = Integer.valueOf(prop.getProperty("puerto"));

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(hostServerName, servicePort);

            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostServerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostServerName);
            System.exit(1);
        } 
        
        
        
        
        
        
        
        
        String nombreUsuario = null;
        
        String entradaTeclado = "";
        String entradaServidor = "";
        String[] entradaServidorDividida;
        int numErrores = 0; 
        char palabra[] = new char[0];
        try { 
            do {
                System.out.println("1. Login");
                System.out.println("2. Registro");
                System.out.println("3. Salir");
                
                opcion = Integer.valueOf(teclado.readLine());

                if (opcion == 1) { 
                    //Pestaña de logeo
                    System.out.println("Introduce tu nombre");
                    String nombre = teclado.readLine();
                    System.out.println("Introduce tu contraseña");
                    String contrasenia = teclado.readLine();
                    
                    out.println("L:"+nombre+":"+contrasenia);  
                    
                    entradaServidor = in.readLine();
                    entradaServidorDividida = entradaServidor.split(":");
                    System.out.println(entradaServidor);
                    
                    if(entradaServidorDividida[0].equals("LC")){
                        //Logeo correcto
                        nombreUsuario = entradaServidorDividida[1];
                        System.out.println(nombreUsuario);
                        System.out.println("Login correcto"); 
                        
                        if(entradaServidorDividida[2].equals("administrador")){
                        
                        
                        
                        }else{
                        
                            do {
                                System.out.println("1. Jugar");
                                System.out.println("2. Salir");
                                System.out.println("");

                                opcion = Integer.valueOf(teclado.readLine());


                                if (opcion == 1) {   

                                    out.println("J:");

                                    entradaServidor = in.readLine();
                                    entradaServidorDividida = entradaServidor.split(":");

                                    if(entradaServidorDividida[0].equals("Longitud")){

                                       palabra = new char[Integer.valueOf(entradaServidorDividida[1])];

                                       for(int i = 0;i<palabra.length;i++){
                                           palabra[i] = '_';
                                       }

                                       System.out.println("Bienvenido al juego, la palabra es la siguiente "+Cliente.dibujarPalabra(palabra));

                                       do{
                                           System.out.println("Introduce una letra o SALIR");

                                           entradaTeclado = teclado.readLine();

                                           char letraUsada = ' ';

                                           if(entradaTeclado.equals("SALIR")){
                                               out.println("S:");

                                           }else{

                                               letraUsada = entradaTeclado.charAt(0);

                                               out.println("L:"+letraUsada);  


                                           } 


                                           entradaServidor = in.readLine();
                                           entradaServidorDividida = entradaServidor.split(":");
                                           

                                           if(entradaServidorDividida[0].equals("LC")){
                                               System.out.println("La letra "+letraUsada+" aparece "+(entradaServidorDividida.length-1 )+ " veces");

                                               for(int i = 1;i <entradaServidorDividida.length;i++){

                                                   palabra[Integer.parseInt(entradaServidorDividida[i])] = letraUsada;


                                                   for(int j = 0;j<palabra.length;j++){
                                                       if(i == Integer.parseInt(entradaServidorDividida[i])){ 
                                                           palabra[i] = letraUsada;
                                                       } 

                                                   }

                                               }
                                               System.out.println(Cliente.dibujarPalabra(palabra));


                                           }else if(entradaServidorDividida[0].equals("LI")){
                                               System.out.println("La letra "+letraUsada+" no aparece");
                                               System.out.println(Cliente.dibujarErrores(Integer.valueOf(entradaServidorDividida[1])));
                                               System.out.println(Cliente.dibujarPalabra(palabra));

                                           }else if(entradaServidorDividida[0].equals("LU")){
                                               System.out.println("Esa letra ya ha sido usada"); 
                                               System.out.println(Cliente.dibujarPalabra(palabra));
                                           }else if(entradaServidorDividida[0].equals("G")){
                                               System.out.println("Felicidadeees!!! Has ganado "+entradaServidorDividida[1]+" puntos");
                                               break;

                                           }else if(entradaServidorDividida[0].equals("P")){
                                               System.out.println(Cliente.dibujarErrores(Integer.valueOf(entradaServidorDividida[1])));
                                               System.out.println("Has perdido :( ");
                                               break;
                                           }else if(entradaServidorDividida[0].equals("S")){ 

                                               System.out.println("Saliendo de la partida");
                                               break;

                                           }  
                                       }while(true);     
                                   } 

                               }else if(opcion == 2){

                                   out.println("S:");  

                                   entradaServidor = in.readLine();
                                   entradaServidorDividida = entradaServidor.split(":");

                                   if(entradaServidorDividida[0].equals("S")){


                                       System.out.println("Hasta la proxima :)");
                                       break;

                                   } 

                                }
                            } while (true);
                            
                        }
                    }else if (entradaServidorDividida[0].equals("LI")){
                        //Logeo incorrecto
                        String motivo = entradaServidorDividida[1];
                        
                        if(motivo.equals("U")){
                            //Usuario
                            System.out.println("El usuario no existe");
                        }else{
                            //Contraseña
                            System.out.println("La contraseña es erronea");
                        } 
                    } 
                    
                }else if(opcion == 2){ 
                    //Pestaña de registro
                    System.out.println("Introduce tu nombre");
                    String nombre = teclado.readLine();
                    System.out.println("Introduce tu contraseña");
                    String contrasenia = teclado.readLine();
                    
                    
                    out.println("R:"+nombre+":"+contrasenia);
                    
                    entradaServidor = in.readLine();
                    entradaServidorDividida = entradaServidor.split(":");
                    
                    if(entradaServidorDividida[0].equals("RC")){
                        //Registro correcto 
                        System.out.println("Registro correcto"); 
                        
                    }else if (entradaServidorDividida[0].equals("RI")){
                        //Registrp incorrecto
                         
                        System.out.println("Registro incorrecto el usuario existe");
                         
                    
                    }  

                }else if(opcion == 3){
                    //Pestaña de salida de la app
                    out.println("S:");
                    
                    entradaServidor = in.readLine();
                    entradaServidorDividida = entradaServidor.split(":");
                    
                    if(entradaServidorDividida[0].equals("S")){  
                        
                        System.out.println("Saliendo de la aplicacion");
                        break;

                    } 
                }
            } while (true); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
    
    
    
    
    
    //METODOS ESTATICOS
    
    
    
    
    
    
    
    public static String dibujarPalabra(char[] letras){
        String palabraConcatenada = "";
            for(int i = 0;i<letras.length;i++){
                if(i == 0){

                    palabraConcatenada+="[";
                    palabraConcatenada+=letras[i];
                    palabraConcatenada+=",";

                }else if(i == letras.length-1){

                    palabraConcatenada+=letras[i];
                    palabraConcatenada+="]";

                }else{

                    palabraConcatenada+=letras[i];
                    palabraConcatenada+=",";
                }

            }
        return palabraConcatenada;
    }
    
    public static String dibujarErrores(int numeroErrores){
        String dibujo = null;
        
        
        switch(numeroErrores){
            case 1:
            dibujo="\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "====\n";
                
                break;
            case 2:
            dibujo="\n"
                + "|\n"
                + "|\n"
                + "|\n"
                + "|\n"
                + "====\n";
                break;
            case 3:
            dibujo="------|\n"
                + "|\n"
                + "|\n"
                + "|\n"
                + "|\n"
                + "====\n";
                break;
            case 4:
            dibujo="------|\n"
                + "|      O\n"
                + "|\n"
                + "|\n"
                + "|\n"
                + "====\n";  
                break;
            case 5:
            dibujo="------|\n"
                + "|      O\n"
                + "|     /|\\  \n"
                + "|\n"
                + "|\n"
                + "====\n";  
                break;
            case 6:
            dibujo="------|\n"
                + "|      O\n"
                + "|     /|\\  \n"
                + "|     / \\  \n"
                + "|\n"
                + "====\n";  
                break; 
        }
        return dibujo;
    }
}