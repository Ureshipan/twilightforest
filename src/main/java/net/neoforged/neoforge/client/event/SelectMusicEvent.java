package net.neoforged.neoforge.client.event;
import net.minecraft.client.sounds.MusicInfo;
public class SelectMusicEvent {
    private MusicInfo music;
    public SelectMusicEvent(MusicInfo music) { this.music = music; }
    public MusicInfo getOriginalMusic() { return music; }
    public void setMusic(MusicInfo music) { this.music = music; }
}