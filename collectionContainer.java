package com.rosamusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class collectionContainer 
{
    // stores the full collection, which will get divided into their own arraylists
    private ArrayList<recordRelease> fullCollection;
    // arraylists that store the collection based on format 
    private ArrayList<recordRelease> vinylLP, vinylEP, cd, cassette, digital, shellac;

    /*
     * 
     * Stores which ArrayLists should be sorted by genre, will be used to modify sortByGenre in recordRelease.java
     * 
     * 0 = vinylLP
     * 1 = vinylEP
     * 2 = cd
     * 3 = cassette
     * 4 = digital
     * 5 = shellac 
     */
    public static boolean[] genreSortList = {false,false,false,false,false,false};

    // constructor only initializes objects, does not assign values 
    // tabula rasa
    public collectionContainer()
    {
        fullCollection = new ArrayList<>();
        vinylLP = new ArrayList<>();
        vinylEP = new ArrayList<>();
        cd = new ArrayList<>();
        cassette = new ArrayList<>();
        digital = new ArrayList<>();
        shellac = new ArrayList<>();
    }

    // getters
    ArrayList<recordRelease> getFullCollection() {return fullCollection;}
    ArrayList<recordRelease> getVinylLP() {return vinylLP;}
    ArrayList<recordRelease> getVinylEP() {return vinylEP;}
    ArrayList<recordRelease> getCD() {return cd;}
    ArrayList<recordRelease> getCassette() {return cassette;}
    ArrayList<recordRelease> getDigital() {return digital;}
    ArrayList<recordRelease> getShellac() {return shellac;}

    // adds an item to the collection
    public void addItem(recordRelease x)
    {
        fullCollection.add(x); // will write a seperate function to update this, this will remain unsorted

        // sorts the item into their respective list 
        switch(x.format)
        {
            case VINYL_LP:
                vinylLP.add(x);
                recordRelease.sortByGenre = genreSortList[0];
                Collections.sort(vinylLP);
                break;
            case VINYL_EP:
                vinylEP.add(x);
                recordRelease.sortByGenre = genreSortList[1];
                Collections.sort(vinylEP);
                break;
            case CD:
                cd.add(x);
                recordRelease.sortByGenre = genreSortList[2];
                Collections.sort(cd);
                break;
            case CASSETTE:
                cassette.add(x);
                recordRelease.sortByGenre = genreSortList[3];
                Collections.sort(cassette);
                break;
            case DIGITAL:
                digital.add(x);
                recordRelease.sortByGenre = genreSortList[4];
                Collections.sort(digital);
                break;
            case SHELLAC:
                shellac.add(x);
                recordRelease.sortByGenre = genreSortList[5];
                Collections.sort(shellac);
                break;
        }
    }

    // NOTE
    // THIS FUNCTION DOES NOT CREATE A PROPER JSON FILE, AS IT STORES MULTIPLE OBJECTS IN A SINGLE FILE
    // THIS DOESN'T MATTER, IGNORE ANY PROBLEMS THAT THE JSON FILE MIGHT SAY THAT IT HAS
    public void save(String filePath)
    {
        // create the file! 
        try
        {
            File newFile = new File(filePath);
            if (newFile.createNewFile())
            {
                System.out.println("FILE SUCCESSFULLY CREATED");
            }
            else 
            {
                System.out.println("FILE ALREADY EXISTS");
                // clear file contents
                try
                {
                    // solution found here: https://www.baeldung.com/java-delete-file-contents
                    new FileWriter(filePath, false).close();
                    System.out.println("FILE CONTENTS SUCCESSFULLY CLEARED");
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();

        // write to file! 
        try
        {
            FileWriter fw = new FileWriter(filePath);
            // save genre settings
            String temp = gson.toJson(genreSortList) + "\n";

            // save albums
            
            fullCollection.clear();

            fullCollection.addAll(vinylLP);
            fullCollection.addAll(vinylEP);
            fullCollection.addAll(cd);
            fullCollection.addAll(cassette);
            fullCollection.addAll(digital);
            fullCollection.addAll(shellac);

            for (recordRelease rr : fullCollection)
            {
                temp += gson.toJson(rr) + "\n";
            }

            temp = temp.substring(0,temp.length() - 1);

            fw.write(temp);
            fw.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void read(String filePath)
    {
        Gson gson = new GsonBuilder().create();

        try
        {
            File rFile = new File(filePath);
            Scanner scn = new Scanner(rFile);

            int idx = 0;

            while (scn.hasNextLine())
            {
                String temp = scn.nextLine();
                if (idx == 0) // genre settings
                {
                    genreSortList = gson.fromJson(temp,boolean[].class);
                }
                else // albums
                {
                    addItem(gson.fromJson(temp,recordRelease.class));
                }
                idx++;
            }

            scn.close();
        }
        catch (FileNotFoundException fnf)
        {
            fnf.printStackTrace();
        }
    }

}