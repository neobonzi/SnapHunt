package com.example.snaphunt;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GamesOverviewInvitesAdapter extends BaseAdapter {
	private Context context;
	private List<Game> invites;


	public GamesOverviewInvitesAdapter(Context context, List<Game> invites){
		this.context = context;
		this.invites = invites;

	}
	
	@Override
	public int getCount() {
		return invites.size();
	}

	@Override
	public Object getItem(int position) {
		return invites.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GameView gameView = null;
		
		if (convertView == null) {
			gameView = new GameView(this.context, this.invites.get(position));
		}
		else {
			gameView = (GameView)convertView;
		}
		gameView.setGame(this.invites.get(position));
		return gameView;
	}
	
}
