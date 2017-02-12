// Fahad Syed 			 | netid: fbs14
// Abdulellah Abualshour | netid: aha59

package view;

// Imported libraries:
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.Song;

public class Control {
    
	// Widgets:
	@FXML Button b1; // add
	@FXML Button b2; // edit
	@FXML Button b3; // delete
	@FXML Button b4; // confirm
	@FXML Button b5; // cancel
	@FXML TextField t1; // name
	@FXML TextField t2; // artist
	@FXML TextField t3; // album
	@FXML TextField t4; // year
	@FXML ListView<Song> songView; // UI song list
	@FXML Label status; // status for user instructions
	
	// Necessary objects and fields:
	private ObservableList<Song> songList; // song objects saved here
	private Song selectedSong; // selected song indicator
	private int selectedSongIndex; // selected song index
	private String saved_t1; // holder
	private String saved_t2; // holder
	private String saved_t3; // holder
	private String saved_t4; // holder
	private int confirm_flag; // to indicate which stage is confirm called on
	private Stage myStage; // UI stage
	private File myFile; // file to load
	
	// Main stage initiation:
	public void start(Stage mainStage) { 
		this.myStage = mainStage; 
		this.confirm_flag = 0; 
		songList = FXCollections.observableArrayList();
		loadSongList(myFile);
		FXCollections.sort(songList);
		songView.setItems(songList);  
		
		 // Set listener for the items:
		songView.getSelectionModel().selectedIndexProperty()
		   .addListener((obs, oldVal, newVal) -> updateTextFields(mainStage));
		
		 // Select the first item:
		songView.getSelectionModel().select(0);

		if(songList.isEmpty() == true){
			b2.setVisible(false);
			b3.setVisible(false);
			b4.setVisible(false);
			b5.setVisible(false);
		}
		
		t1.setEditable(false);
		t2.setEditable(false);
		t3.setEditable(false);
		t4.setEditable(false);
	}
	
	/*
	 * The method click() catches any event of clicking and handles it accordingly.
	 * Takes ActionEvent as input.
	 */
	public void click(ActionEvent e) {
		if((e.getSource() instanceof Button)){
			Button b = (Button)e.getSource();
			if (b == b1) {
				 status.setText("Please add details above");
				 add();
			 } 
			 else if (b == b2) {
				 status.setText("Please make your modifications above");
				 edit();
			 }	
			 else if (b == b3) {
				status.setText("Please confirm deletion process!");
				delete();
			 }
			 else if (b == b4){
				status.setText("");
				confirm();
				b4.setVisible(false);
				b5.setVisible(false);
			 }
			 else if (b == b5){
				 cancel();
			 }
		}		 
	} 
	
	/*
	 * The add() method for confirming text fields and adding the data as a new song object. 
	 */
	public void add(){
		this.saved_t1 = t1.getText();
		this.saved_t2 = t2.getText();
		this.saved_t3 = t3.getText();
		this.saved_t4 = t4.getText();
		t1.setEditable(true);
		t2.setEditable(true);
		t3.setEditable(true);
		t4.setEditable(true);
		t1.setText("");
		t2.setText("");
		t3.setText("");
		t4.setText("");
		b2.setVisible(false);
		b3.setVisible(false);
		b4.setVisible(true);
		b5.setVisible(true);
		b1.setVisible(false);
		this.confirm_flag = 1; 
	}
	
	/*
	 * The edit() method for handling the clicking event on edit and unlocking the textfields for editing.
	 */
	public void edit(){
		b1.setVisible(false);
		b3.setVisible(false);
		b2.setVisible(false);
		this.saved_t1 = t1.getText();
		this.saved_t2 = t2.getText();
		this.saved_t3 = t3.getText();
		this.saved_t4 = t4.getText();
		t1.setEditable(true);
		t2.setEditable(true);
		t3.setEditable(true);
		t4.setEditable(true);
		b4.setVisible(true);
		b5.setVisible(true);
		this.confirm_flag = 2; 
	}
	
	/*
	 * Boolean method isInList() to see if a song already exists in the list when trying to add or edit.
	 */
	public boolean isInList(Song s){
		
		String name = s.getName().toLowerCase().trim();
		String artist = s.getArtist().toLowerCase().trim(); 
		//check to see if there is a song in the list with the same name
		//if so, check to see if artists are the same. 
		for(int i = 0; i < this.songList.size(); i++){
			
			if(songList.get(i).getName().toLowerCase().trim().compareTo(name) > 0){
				
				return false;
			}
			if(songList.get(i).getName().toLowerCase().trim().compareTo(name) == 0){
				
				return (songList.get(i).getArtist().toLowerCase().trim().compareTo(artist) == 0);
			}
		}
		return false;
	}
	
	/*
	 * The confirm() method determines which state was confirm clicked in and confirm accordingly if it's add, edit, etc.
	 */
	public void confirm(){
		Song item = new Song("", "", "", ""); 
		if(this.confirm_flag == 1){ //adding status
			item = new Song(t1.getText(),t2.getText(),t3.getText(),t4.getText());
			
			if((item.getArtist() == null || item.getArtist().equals(""))
			&& (item.getName() == null || item.getName().equals(""))){
				
				showDialogError(this.myStage, "Cannot add a song with no name or artist.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				}
			}
			else if(item.getArtist() == null || item.getArtist().equals("")){
				
				showDialogError(this.myStage, "Cannot add a song with no artist.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				} 
			}
			else if(item.getName() == null || item.getName().equals("")){
				
				showDialogError(this.myStage, "Cannot add a song with no name.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				} 
			}
			else if(isInList(item)){
				
				showDialogError(this.myStage, "This song is already in the list.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				}
			}
			else{
				songList.add(item);
			}
		}
		else if(this.confirm_flag == 2){ //editing
			//NEED TO CHECK IF NEW SONG EXISTS
			item = songView.getSelectionModel().getSelectedItem();
			this.selectedSong = item;
			int index = songView.getSelectionModel().getSelectedIndex();
			this.selectedSongIndex = index;
			Song newitem = new Song(t1.getText(), t2.getText(), t3.getText(), t4.getText());
			item = newitem; 
			if(isInList(item) && !item.getName().equals(this.saved_t1) && !item.getArtist().equals(this.saved_t2)){
				showDialogError(this.myStage, "This song is already in the list.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				} 
			}
			else if(item.getName().trim().equals("") || item.getArtist().trim().equals("")){
				showDialogError(this.myStage, "Cannot save a song without an artist or name.");
				if(!songList.isEmpty()){
					item = songView.getSelectionModel().getSelectedItem(); 
				}
				
			}
			else{
				songList.set(index, item);
			}
		}
		
		b1.setVisible(true);
		FXCollections.sort(songList);
		t1.setEditable(false);
		t2.setEditable(false);
		t3.setEditable(false);
		t4.setEditable(false);
		this.saved_t1 = "";
		this.saved_t2 = "";
		this.saved_t3 = "";
		this.saved_t4 = "";
		
		if(!(item.getName().equals("")) && !(item.getArtist().equals(""))){
			int index = songList.indexOf(item);
			songView.getSelectionModel().select(index);
			songView.getFocusModel().focus(index);
			songView.scrollTo(index);
		}
		this.confirm_flag = 0; 
	}
	
	/*
	 * The method cancel() for canceling adding or editing.
	 */
	public void cancel(){
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);
		b4.setVisible(false);
		b5.setVisible(false);
		t1.setText(saved_t1);
		t2.setText(saved_t2);
		t3.setText(saved_t3);
		t4.setText(saved_t4);
		t1.setEditable(false);
		t2.setEditable(false);
		t3.setEditable(false);
		t4.setEditable(false);
		status.setText("");
		this.confirm_flag = 0; 
	}
	
	/*
	 * The method delete() to delete a song from the list.
	 */
	public void delete(){
		Optional<ButtonType> val = showDialogConf(this.myStage, "Are you sure you want to delete this song?");
		if (val.get() == ButtonType.OK){ //user chose yes.
			
			int index = selectedSongIndex;
			//if we are deleting the last song
			if(songList.size() <= index){
				index--; 
			}
			//will change selection to the next song, else previous song, else none 
			if(this.selectedSong != null){
				songList.remove(index);
				status.setText("");
				if(!songList.isEmpty()){
					songView.getSelectionModel().select(index);
					songView.getFocusModel().focus(index);
					songView.scrollTo(index);
				}
			}		
		}
		else{
			//do nothing
		}
	}

	/*
	 * The updateTextFields method to update the text fields to have selected song details.
	 */
	private void updateTextFields(Stage mainStage) { 
		
		cancel();
		Song item = songView.getSelectionModel().getSelectedItem();
		this.selectedSong = item;
		int index = songView.getSelectionModel().getSelectedIndex();
		this.selectedSongIndex = index;
		
		if(item == null){
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			b2.setVisible(false);
			b3.setVisible(false);
			b4.setVisible(false);
			t1.setEditable(false);
			t2.setEditable(false);
			t3.setEditable(false);
			t4.setEditable(false);
			return; 
		}
		
		b2.setVisible(true);
		b3.setVisible(true);
		b4.setVisible(false);
		b5.setVisible(false);
		t1.setEditable(true);
		t2.setEditable(true);
		t3.setEditable(true);
		t4.setEditable(true);
		t1.setText(item.getName());
		t2.setText(item.getArtist());
		t3.setText(item.getAlbum());
		t4.setText(item.getYear());
		t1.setEditable(false);
		t2.setEditable(false);
		t3.setEditable(false);
		t4.setEditable(false);

	}

	/*
	 * The method showDialgError() to popup errors.
	 */
	private void showDialogError(Stage mainStage,  String message) {                
		   Alert alert = new Alert(AlertType.ERROR);
		   alert.setTitle("Error");
		   alert.setHeaderText("");
		   alert.setContentText(message);
		   alert.showAndWait();

	   }
	
	/*
	 * The method showDialgConf() to popup confirmation for deletion.
	 */
	private Optional<ButtonType> showDialogConf(Stage mainStage,  String message) {                
		   Alert alert = new Alert(AlertType.CONFIRMATION);
		   alert.setTitle("Attention");
		   alert.setHeaderText("");
		   alert.setContentText(message);
		   Optional<ButtonType> result = alert.showAndWait();
		   
		   return result;
		   
	}
	
	/*
	 * The method getSongList() is a getter for song list.
	 */
	public ObservableList<Song> getSongList(){
		return songList; 
	}
	
	/*
	 * The method loadSongList() is the song list loader from the save file.
	 */
	public void loadSongList(File file){
		FileInputStream filen;
		try {
			filen = new FileInputStream(file);
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(filen));
			int lineCounter = 0; 
			String line = null;
			String name = ""; 
			String artist = "";
			String album = "";
			String year = ""; 
			while ((line = br.readLine()) != null) {
				lineCounter++; 
				if(lineCounter%4 == 1){
					name = line; 
				}
				if(lineCounter%4 == 2){
					artist = line; 
				}
				if(lineCounter%4 == 3){
					album = line;
				}
				else if(lineCounter%4 == 0){
					year = line; 
					songList.add(new Song(name, artist, album, year));
					name = "";
					artist = ""; 
					album = ""; 
					year = ""; 
				}
				
			}
			if(!name.equals("") && !artist.equals("")){
				
				songList.add(new Song(name, artist, album, year));
				name = "";
				artist = ""; 
				album = ""; 
				year = "";
			}
			br.close();
			return; 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		
	}
	
	/*
	 * The method getFile() is a getter for myFile. 
	 */
	public File getFile(){
		return myFile; 
	}
	
	/*
	 * The method setFile() is a setter for myFile. 
	 */
	public void setFile(File file){
		myFile = file; 
	}
}

