package com.example.snaphunt;

//Game object to inflate GameView layout object
public class Game {
<<<<<<< HEAD
	int id;
	private String groupName;
	private String theme;
=======
	public int myint = 0;
>>>>>>> 8d103c88ef582ec542c9984badd605e1c3f70435


	//player id?
	private int currentJudge;
	
	private String p1;
	private String p2;
	private String p3;
	private String p4;
	
	//fill in constructor. 
	public Game(int id, String groupName, String theme, String p1, String p2, String p3, String p4){
		this.id = id;
		this.groupName = groupName;
		this.theme = theme;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
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
	public int getId(){
		return this.id;
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
