// Fahad Syed 			 | netid: fbs14 
// Abdulellah Abualshour | netid: aha59

package application;
	
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import view.Control;
//TODO import view controller

public class SongLib extends Application {
	

	private boolean saveFileExists = false; 
	private Control controller; 
	
	@Override
	public void start(Stage primaryStage) {
		//file creation for saved library
		File file = new File("src/application/SongSave.txt");
		if (!file.exists()) {

			try {
				this.saveFileExists = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/SongUI.fxml"));
		try {
			//this line was causing a problem, but this fixes it. 
			AnchorPane root = (AnchorPane)loader.load();
			controller = loader.getController();
			controller.setFile(file);
	        controller.start(primaryStage);
			
	        Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
	    try{
	    	Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/application/SongSave.txt"), "utf-8"));
		    // Save file
		    ObservableList<Song> list = controller.getSongList();
		    for(int i = 0; i < list.size(); i++){
		    	Song x = list.get(i);
		    	try{
		    	    writer.write(x.getName()+"\n"); //name
		    	    writer.write(x.getArtist()+"\n"); //artist
		    	    writer.write(x.getAlbum()+"\n"); //album
		    	    writer.write(x.getYear()+"\n");//year
		    	} catch (IOException e) {
		    	   // do something
		    	}
		    }
		    try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    catch (IOException e){}
	}
}
