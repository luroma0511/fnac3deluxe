package com.fnac3.deluxe.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioClass {
    Map<String, Long> soundIDs;
    Map<String, Sound> soundMap;
    Map<String, Float> volume;
    Map<String, Float> pitch;

    public AudioClass(){
        soundIDs = new HashMap<>();
        soundMap = new HashMap<>();
        volume = new HashMap<>();
        pitch = new HashMap<>();
    }

    public void createSound(String path){
        if (soundMap.containsKey(path)) return;
        Sound sound = Gdx.audio.newSound(Gdx.files.local("assets/sounds/" + path + ".wav"));
        soundMap.put(path, sound);
    }

    public void play(String path){
        if (soundIDs.containsKey(path)) {
            stop(path);
        }

        float vol = 1;
        long id = soundMap.get(path).play(vol);
        soundIDs.put(path, id);
        volume.put(path, vol);
        pitch.put(path, 1f);
    }

//    public void pause(String path){
//        if (!soundIDs.containsKey(path)) return;
//        soundMap.get(path).pause(soundIDs.get(path));
//    }

    public void stop(String path){
        if (!soundIDs.containsKey(path)) return;
        soundMap.get(path).stop(soundIDs.get(path));
        soundIDs.remove(path);
        pitch.remove(path);
        volume.remove(path);
    }

    public void stopAllSounds(){
        for (String path: soundMap.keySet()){
            stop(path);
        }
    }

    public void loop(String path, boolean loop){
        if (!soundIDs.containsKey(path)) return;
        soundMap.get(path).setLooping(soundIDs.get(path), loop);
    }

    public float getVolume(String path){
        if (!volume.containsKey(path)) return 0;
        return volume.get(path);
    }

    public void setVolume(String path, float volume){
        if (!soundIDs.containsKey(path)) return;
        soundMap.get(path).setVolume(soundIDs.get(path), volume);
        this.volume.put(path, volume);
    }

    public float getPitch(String path){
        if (!pitch.containsKey(path)) return 0;
        return pitch.get(path);
    }

    public void setPitch(String path, float pitch){
        if (!soundIDs.containsKey(path)) return;
        soundMap.get(path).setPitch(soundIDs.get(path), pitch);
        this.pitch.put(path, pitch);
    }
}
