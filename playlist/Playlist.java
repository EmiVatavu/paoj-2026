package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name){
        this.name = name;
        this.songs = new Song[0];
    }

    public void addSong(Song song){
        Song[] copySongs = new Song[songs.length + 1];
        System.arraycopy(songs, 0, copySongs, 0, songs.length);

        copySongs[songs.length] = song;
        songs = copySongs;
    }

    public void printSortedByTitle(){
        Song[] copySongs = songs.clone();
        Arrays.sort(copySongs);
        for(Song spongebob: copySongs){
            System.out.println(spongebob);
        }
    }

    public void printSortedByDuration(){
        Song[] copySongs = songs.clone();
        Arrays.sort(copySongs, new SongDurationComparator());
        for(Song patrick: copySongs){
            System.out.println(patrick);
        }
    }

    public int getTotalDuration(){
        int secunde = 0;
        for(Song calamar: songs){
            secunde = secunde + calamar.durationSeconds();
        }
        return secunde;
    }

    public String getName(){
        return this.name;
    }
}
