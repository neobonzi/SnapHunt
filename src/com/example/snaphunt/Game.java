package com.example.snaphunt;

//Game object to inflate GameView layout object
public class Game {
	private String groupName;
	private String theme;

	//player id?
	private int currentJudge;
	
	private String p1;
	private String p2;
	private String p3;
	private String p4;
	
	//fill in constructor. 
	public Game(){
		
	}
	
	public void setJudge(int judge){
		//check for validity?
		currentJudge = judge;
	}
	public void setTheme(String theme){
		this.theme = theme;
	}
	
	public String getGroupName(){
		return this.groupName;
	}
	public String getP1(){
		return this.p1;
	}
	public String getP2(){
		return this.p2;
	}	
	public String getP3(){
		return this.p3;
	}	
	public String getP4(){
		return this.p4;
	}	
	public String getTheme(){
		return this.theme;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Game)
		{
			Game game2 = (Game)obj;
			return(this.groupName.equalsIgnoreCase(game2.groupName) &&
					this.theme.equals(game2.theme) &&
					this.p1 == game2.p1 &&
					this.p2 == game2.p2 &&
					this.p3 == game2.p3 &&
					this.p4 == game2.p4);
		}
		return false;
	}
}
