package com.rosamusic;

import java.util.ArrayList;

// needs to store title, artists, genres, format and release date
public class recordRelease implements Comparable<recordRelease>
{
    enum Format
    {
        VINYL_LP, // 12" and 10" 
        VINYL_EP, // 7" 
        CD, 
        CASSETTE,
        DIGITAL,
        SHELLAC
    }

    public String title;
    public ArrayList<String> artists;
    public ArrayList<String> genres;
    public Date releaseDate;
    public Format format; // will grab values from an enum

    // this variable exists to tell the program whether or not a certain arraylist should be sorted by genre or not
    // when sorting an arraylist, i can swap this value beforehand to get the desired result 
    public static boolean sortByGenre = true;

    //--PRIVATE ACCESS FUNCTIONS--//

    // used to compare strings alphanumerically in the compareTo function
    private int stringCompare(String stringA, String stringB)
    {
        if (stringA.length() > 0 && stringB.length() > 0)
        {

            if (!stringA.equals(stringB))
            {
                char listA[] = stringA.toCharArray();
                char listB[] = stringB.toCharArray();

                int size = (listA.length < listB.length) ?
                    listA.length :
                    listB.length;

                for(int i = 0; i < size; ++i)
                {
                    if (listA[i] < listB[i])
                    {
                        return -1;
                    }
                    else if (listA[i] > listB[i])
                    {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    //--PUBLIC ACCESS FUNCTIONS--//

    // constructor A
    public recordRelease(String paramTitle, ArrayList<String> paramArtists, ArrayList<String> paramGenres, Date paramRDate, Format paramFormat)
    {
        title = paramTitle;
        artists = paramArtists;
        genres = paramGenres;
        releaseDate = paramRDate;
        format = paramFormat;
    }

    // constructor B, this will mainly be used for testing 
    public recordRelease(String paramTitle, String paramArtist, String paramGenre, Date paramRDate, Format paramFormat)
    {
        title = paramTitle;
        artists = new ArrayList<String>();
        artists.add(paramArtist);
        genres = new ArrayList<String>();
        genres.add(paramGenre);
        releaseDate = paramRDate;
        format = paramFormat;
    }

    public String formatToString()
    {
        String temp = "";
        switch(format)
        {
            case VINYL_LP:
                temp += "12\" or 10\" Vinyl Record.";
                break;
            case VINYL_EP:
                temp += "7\" Vinyl Record.";
                break;
            case CD:
                temp += "CD.";
                break;
            case CASSETTE:
                temp += "Cassette Tape.";
                break;
            case DIGITAL:
                temp += "Digital File.";
                break;
            case SHELLAC:
                temp += "Shellac Record.";
                break;
        }
        return temp;
    }

    public recordReleaseStringRep createStringRep()
    {
        return new recordReleaseStringRep(this);
    }

    // print me! 
    @Override 
    public String toString()
    {
        String temp = "";

        temp += title + "\n";

        if (artists.size() > 0)
        {
            for (int i = 0; i < artists.size(); ++i)
            {
                temp += artists.get(i);
                if (i < artists.size() - 1) temp += ", ";
            }
            temp += "\n";
        }
        else temp += "Unknown artist.\n";

        if (genres.size() > 0)
        {
            for (int i = 0; i < genres.size(); ++i)
            {
                temp += genres.get(i);
                if (i < genres.size() - 1) temp += ", ";
            }
            temp += "\n";
        }
        else temp += "Unknown genre.\n";

        temp += "Released " + releaseDate.toString() + "\n";

        temp += formatToString() + "\n";

        return temp;
    }



    /*
     * Order of operations for compareTo:
     * Format
     * First Genre IF sortByGenre == true
     * First Artist
     * Release Date
     * Title
     * 
     * Differs from the original C++ Program since this WILL take format into account
     */

    // returns -1 if less than
    // returns 0 if equal
    // returns 1 if greater than
    @Override 
    public int compareTo(recordRelease otherRR)
    {
        int tempNum = 0; // will be used in situations where we need to comapre with the compareTo function
        String tempTextA = ""; // will be used when we compare the first item in an ArrayList of strings
        String tempTextB = "";

        // Format
        tempNum = this.format.compareTo(otherRR.format);
        if (tempNum != 0) return tempNum;

        // First Genre
        if (sortByGenre == true && this.genres.size() > 0 && otherRR.genres.size() > 0)
        {
            tempTextA = this.genres.get(0).toUpperCase();
            tempTextB = otherRR.genres.get(0).toUpperCase();

            tempNum = stringCompare(tempTextA, tempTextB);
            if (tempNum != 0) return tempNum;
        }

        // First Artist
        if (this.artists.size() > 0 && otherRR.artists.size() > 0)
        {
            tempTextA = this.artists.get(0).toUpperCase();
            tempTextB = otherRR.artists.get(0).toUpperCase();

            tempNum = stringCompare(tempTextA, tempTextB);
            if (tempNum != 0) return tempNum;
        }

        // Release Date
        tempNum = this.releaseDate.compareTo(otherRR.releaseDate);
        if (tempNum != 0) return tempNum;

        // Title

        tempTextA = this.title.toUpperCase();
        tempTextB = otherRR.title.toUpperCase();

        tempNum = stringCompare(tempTextA, tempTextB);
        if (tempNum != 0) return tempNum;

        // equal entries
        return 0;
    }
}
