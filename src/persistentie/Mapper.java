package persistentie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import domein.Game;



public class Mapper implements java.io.Serializable{

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/coloretto?user=root&password=root&serverTimezone=UTC&useSSL=false";

   
    
   
    
    public static void setHighScoreToDatabase(String playerName, int score) {
    	try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
    		
    		conn.createStatement().executeUpdate(String.format("INSERT INTO highscores (PlayerName, Score) VALUES ('%s', %s);",playerName, score));

       	
       } catch (SQLException ex) {
       	System.out.println("connectie mislukt");
           for (Throwable t : ex) {
               t.printStackTrace();
           }
       }
    }
    
    
    
    public static String getHighScoresFromDatabase() {
    	String highScores = "";
    	try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
         	ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM highscores ORDER BY Score DESC");
         	int placeCounter = 1;
         	
         	while(rs.next() && placeCounter <= 10){
			    //Retrieve by column name
			 
			    //Display values
         		highScores += placeCounter++ + ". " + rs.getString("PlayerName") + ": " + rs.getInt("Score") + "\n" ;
		
			}
         } catch (SQLException ex) {
         	System.out.println("connectie mislukt");
             for (Throwable t : ex) {
                 t.printStackTrace();
             }
         }

    	return highScores;
    }
    
    //slaat bestaande game op in database
    //bij aanroepen game casten naar object: ((Object) game))
    public static void saveGame(Object game) {
    	 try (Connection conn = DriverManager.getConnection(JDBC_URL))  {    
     		 conn.createStatement().executeUpdate("DELETE FROM objects");   
             FileOutputStream fos = new FileOutputStream("savedGame.txt"); 
             ObjectOutputStream oout = new ObjectOutputStream(fos);                
             oout.writeObject(game);                
             oout.close(); 
             fos.close();              
             String filePath = "savedGame.txt";
             InputStream inputStream = new FileInputStream(new File(filePath));              
             String sql = "INSERT INTO objects (id, object) values (?,?)";
             PreparedStatement statement = conn.prepareStatement(sql);
             statement.setInt(1, 1);
             statement.setBlob(2, inputStream);             
             statement.executeUpdate();
         } 
    	 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
    	 }
         catch(IOException ex) 
         { 
        	 ex.printStackTrace();
         } 
    	
   
    }
    //haalt de opgeslagen game op uit de database als Game object
    public static Game getSavedGame() {
    	

    	
    	
    	// Deserialization 
    	Game game = null;
    	 ResultSet rs = null;
         try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
             rs = conn.createStatement().executeQuery("SELECT * FROM objects");

             // write binary stream into file
             File file = new File("savedGame.txt");
             FileOutputStream output = new FileOutputStream(file);

             while (rs.next()) {
                 InputStream input = rs.getBinaryStream("object");
                 byte[] buffer = new byte[1024];
                 while (input.read(buffer) > 0) {
                     output.write(buffer);
                 }
             }
         } catch (SQLException | IOException e) {
             System.out.println(e.getMessage());
         } finally {
             try {
                 if (rs != null) {
                     rs.close();
                 }
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
         }
    	
        try (Connection conn = DriverManager.getConnection(JDBC_URL)){ 
         	
         	
         	
            // Reading the object from a file 
            FileInputStream file = new FileInputStream 
                                         ("savedGame.txt"); 
            ObjectInputStream in = new ObjectInputStream 
                                         (file); 
  
            // Method for deserialization of object 
            game = (Game)in.readObject(); 
  
            in.close(); 
            file.close(); 
            System.out.println("Object has been deserialized\n"
                                + "Data after Deserialization."); 
            
            
        } 
  
        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
  
        catch (ClassNotFoundException ex) { 
            System.out.println("ClassNotFoundException" + 
                                " is caught"); 
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return game;
    }
    //het opgeslagen spel verwijderen uit de database
    public static void deleteSavedGame() {
    	
		 try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
			 conn.createStatement().executeUpdate("DELETE FROM objects");

	        } catch (SQLException ex) {
	            for (Throwable t : ex) {
	                t.printStackTrace();
	            }
	        }
    }
    //checkt of er een game opgeslag
    public static boolean savedGameExists() {
    	boolean exists = false;
    	 try (Connection conn = DriverManager.getConnection(JDBC_URL)) {

             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM objects");
             exists = rs.next();
             
		     
         } catch (SQLException ex) {
         	System.out.println("connectie mislukt");
             for (Throwable t : ex) {
                 t.printStackTrace();
             }
         }
    	return exists;
    }


}
