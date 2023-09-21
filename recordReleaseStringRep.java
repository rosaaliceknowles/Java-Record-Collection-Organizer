package com.rosamusic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// representation of recordRelease that will be used in the table
// can easily convert from a recordRelease object to a recordReleaseStringRep object

public class recordReleaseStringRep 
{
    private StringProperty title, artists, genres, releaseDate, format;

    // needed so javafx can find the properties
    public String getTitle() {return title.get();}
    public StringProperty getTitleProperty() {return title;}
    public String getArtists() {return artists.get();}
    public StringProperty getArtistsProperty() {return artists;}
    public String getGenres() {return genres.get();}
    public StringProperty getGenresProperty() {return genres;}
    public String getReleaseDate() {return releaseDate.get();}
    public StringProperty getReleaseDateProperty() {return releaseDate;}
    public String getFormat() {return format.get();}
    public StringProperty getFormatProperty() {return format;}

    public recordReleaseStringRep(recordRelease rr)
    {
        title = new SimpleStringProperty(rr.title);

        String temp;

        temp = "";
        for (int i = 0; i < rr.artists.size(); ++i)
        {
            temp += rr.artists.get(i);
            if (i < rr.artists.size() - 1) temp += ", ";
        }

        artists = new SimpleStringProperty(temp);

        temp = "";
        for (int i = 0; i < rr.genres.size(); ++i)
        {
            temp += rr.genres.get(i);
            if (i < rr.genres.size() - 1) temp += ", ";
        }

        genres = new SimpleStringProperty(temp);

        releaseDate = new SimpleStringProperty(rr.releaseDate.toString());

        format = new SimpleStringProperty(rr.formatToString().substring(0,rr.formatToString().length() - 1));
    }

    @Override
    public boolean equals(Object O)
    {
        // casts Object O to a recordReleaseStringRep
        final recordReleaseStringRep rrsr = (recordReleaseStringRep)O;

        if (this.title.get().equals(rrsr.title.get()) && this.artists.get().equals(rrsr.artists.get()) && this.genres.get().equals(rrsr.genres.get()) && this.releaseDate.get().equals(rrsr.releaseDate.get()) && this.format.get().equals(rrsr.format.get())) return true;
        return false;
    }
}
