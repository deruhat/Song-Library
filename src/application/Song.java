// Fahad Syed			 | netid: fbs14 
// Abdulellah Abualshour | netid: aha59

package application;

public class Song implements Comparable<Song>{
	//fields
	private String name;
	private String artist;
	private String album;
    private String year;
    
    //constructor
    public Song(String name, String artist, String album, String year){
    	this.name = name;
    	this.artist = artist;
    	this.album = album;
    	this.year = year;
    }
    
    //getters and setters
    public String getName(){
    	return name;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public String getArtist(){
    	return artist;
    }
    
    public void setArtist(String artist){
    	this.artist = artist;
    }
    
    public String getAlbum(){
    	return album;
    }
    
    public void setAlbum(String album){
    	this.album = album;
    }
    
    public String getYear(){
    	return year;
    }
    
    public void setYear(String year){
    	this.year = year;
    }
    
    //helper methods
    public int compareTo(Song x){
    	return name.toLowerCase().compareTo(x.name.toLowerCase());
    }
    
    public String toString(){
    	return name;
    }
    
    //also implement equals method
    public boolean equals(Object obj){
    	if(obj == null || !(obj instanceof Song)){
    		return false;
    	}
    	Song o = (Song)obj; 
    	
    	return (this.compareTo(o) == 0); 
    }
}
