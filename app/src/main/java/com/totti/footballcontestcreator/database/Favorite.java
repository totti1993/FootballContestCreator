package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
	 /* foreignKeys = {@ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "favorite_team_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
				       @ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "favorite_tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)} */
public class Favorite {

	// ID of the favorite item (ID of the item)
	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	// ID of the favorite team
	@ColumnInfo(name = "favorite_team_id")
	private String favorite_team_id;

	// ID of the favorite tournament
	@ColumnInfo(name = "favorite_tournament_id")
	private String favorite_tournament_id;

	// Type of the favorite item: "team" or "tournament"
	@ColumnInfo(name = "favorite_type")
	private String favorite_type;

	@Ignore
	public Favorite() {
		// Default constructor required for DataSnapshot.getValue(Favorite.class)
	}

	public Favorite(@NonNull String id, String favorite_team_id, String favorite_tournament_id, String favorite_type) {
		this.id = id;
		this.favorite_team_id = favorite_team_id;
		this.favorite_tournament_id = favorite_tournament_id;
		this.favorite_type = favorite_type;
	}

	@NonNull
	public String getId() {
		return this.id;
	}

	public void setId(@NonNull String id) {
		this.id = id;
	}

	public String getFavorite_team_id() {
		return this.favorite_team_id;
	}

	public void setFavorite_team_id(String favorite_team_id) {
		this.favorite_team_id = favorite_team_id;
	}

	public String getFavorite_tournament_id() {
		return this.favorite_tournament_id;
	}

	public void setFavorite_tournament_id(String favorite_tournament_id) {
		this.favorite_tournament_id = favorite_tournament_id;
	}

	public String getFavorite_type() {
		return this.favorite_type;
	}

	public void setFavorite_type(String favorite_type) {
		this.favorite_type = favorite_type;
	}
}
