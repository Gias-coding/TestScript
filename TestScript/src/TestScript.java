import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Random;
public class TestScript {
	
	
	
	
	public static void cancella_righe(Path directory,String estensione[],int perc_lines,int perc_chars ) {
		if(!(Files.isDirectory(directory))) {
			throw new NullPointerException("Directory inesistente");
		}
		try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory); //apertura flusso per manipolazione sulla cartella
            for (Path entry : stream) { //ciclo che agisce su file o sottocartelle
            	 if (Files.isDirectory(entry)) {  // trovata sottocartella, ricorsione sulla sottocartella
                     cancella_righe(entry,estensione,perc_lines,perc_chars);
                 } else if (Files.isRegularFile(entry)) {  // è un file , può essere manipolato
                	 boolean verifica=false;//condizione che servirà a verificare se ci sono estensioni passate come parametro evitando ridondanza di codice
                	 if(estensione.length==0) {//nessuna estensione, il codicè verrà eseguito su tutti i file
                		 verifica=true;
                	 }
                	 else {
                		 verifica = false;
                		 String nomeFile=entry.getFileName().toString();//ricaviamo l'estensione per confrontarla con quelle accettate
                    	 int indiceEstensione=nomeFile.lastIndexOf(".");
                    	 String est=nomeFile.substring(indiceEstensione+1);
                		 for(String i:estensione) {
                         	if(est.equals(i)) {
                         		verifica=true;
                         	}
                         }
                	 }
                    	 if(verifica) {
                    		 List<String> righe = Files.readAllLines(entry);//otteniamo il numero di righe presenti nel file
                             String contenuto= Files.readString(entry);//otteniamo il numero di caratteri presenti nel file
                             Random random=new Random();// randomizzatore , ci servirà successivamente
                             int righeDeleter= (righe.size() * perc_lines)/100;//numero  di righe da eliminare
                             int numerocasuale=0;
                             while(righeDeleter!=0) {
                            	 numerocasuale=random.nextInt(Files.readAllLines(entry).size());//scelta casuale riga da rimuovere
                            	 righe.remove(numerocasuale);
                            	 Files.write(entry,righe);//modifica effettuata
                            	 righeDeleter--;
                             }
                             contenuto= Files.readString(entry);//otteniamo l'intero testo su una sola stringa 
                             int charDeleter=(contenuto.length()*perc_chars)/100;//numero di caratteri da sostituire
                             while(charDeleter!=0) {
                            	 numerocasuale=random.nextInt(contenuto.length());
                            	 char carattereCasuale = (char) ('a' + random.nextInt(26));//carattere casuale da inserire nel testo
                            	 StringBuilder sostitutore=new StringBuilder(contenuto);//Lavoriamo con una nuova stringa
                            	 sostitutore.setCharAt(numerocasuale, carattereCasuale);
                            	 contenuto = sostitutore.toString();
                            	 charDeleter--;
                             }
                             Files.write(entry,contenuto.getBytes());//rendiamo accessibile contenuto a files.write
                    	 }
                    
                       
                 }
               
            }
            stream.close();
            System.out.println("Codice eseguito");
        } catch (IOException e) {
            System.out.println("Errore nell'elenco dei contenuti: " + e.getMessage());
        }
	
	}
	public static void cancella_righe(Path directory) {
		String[] vuota=new String[0];
		cancella_righe(directory,vuota,5,5);//esecuzione script con parametri default
	}
	
	public static void cancella_righe() {
		throw new NullPointerException("Directory inesistente");
	}

	
	
	public static void main(String[] args) {
		Path cartella = Paths.get("provaSorgenti");
		
		String[] estensione = {"txt", "js"};
		int righe=10;
		int caratteri=20;
		cancella_righe(cartella,estensione,righe,caratteri);


		
	}

}
