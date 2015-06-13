package com.example.boostlanguage.entity;

public class Sentences {

	private long id ;
	private String world;
	private String worldTrans;
	
	public Sentences(){
		
	}
	
	public Sentences(String world, String worldTrans){
		this.world = world;
		this.worldTrans = worldTrans;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public String getWorldTrans() {
		return worldTrans;
	}
	public void setWorldTrans(String worldTrans) {
		this.worldTrans = worldTrans;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return world;
	  }
}
