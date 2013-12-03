package com.example.snaphunt;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GamesOverviewAdapter extends BaseAdapter {
	private Context context;
	private List<Game> games;
	

	public GamesOverviewAdapter(Context context, List<Game> games){
		this.context = context;
		this.games = games;

	}
	
	@Override
	public int getCount() {
		return games.size();
	}

	@Override
	public Object getItem(int position) {
		return games.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GameView gameView = null;
		
		if (convertView == null) {
			gameView = new GameView(this.context, this.games.get(position));
		}
		else {
			gameView = (GameView)convertView;
		}
		gameView.setGame(this.games.get(position));
		return gameView;
	}
	
}
