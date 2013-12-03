package com.example.snaphunt;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameView extends LinearLayout  {
	
	private Game game;
	
	private TextView groupName;
	private TextView themeText;
	private TextView p1;
	private TextView p2;
	private TextView p3;
	private TextView p4;

	
	public GameView(Context context, Game game){
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_gameview, this, true);
		this.groupName = (TextView)findViewById(R.id.gameveiw_groupname);
		this.themeText = (TextView)findViewById(R.id.gameview_current_theme);
		this.p1 = (TextView)findViewById(R.id.gameview_p1);
		this.p2 = (TextView)findViewById(R.id.gameview_p2);
		this.p3 = (TextView)findViewById(R.id.gameview_p3);
		this.p4 = (TextView)findViewById(R.id.gameview_p4);
		this.setGame(game);
		
	}


	private void setGame(Game game) {
		this.game = game;
		groupName.setText(game.getGroupName());
		themeText.setText(game.getTheme());
		p1.setText(game.getP1());
		p2.setText(game.getP2());
		p3.setText(game.getP3());
		p4.setText(game.getP4());

	}
}