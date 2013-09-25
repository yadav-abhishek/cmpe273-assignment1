package edu.sjsu.cmpe.library.domain;




public class Author {
    public long id;
    
    
    private String name;
    
    //  @return the id
     
    public long getID() {
	return id;
    }

   
   public void setID(long id) {
	this.id = id;
    }

   
    public String getName() {
	return name;
    }

   
    public void setName(String name) {
	this.name = name;
    }
  
}