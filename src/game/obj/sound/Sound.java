//Sound package
package game.obj.sound;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound
{


    //Access Specifiers of these 3 sounds of gun
    private final URL shoot;
    private final URL hit;
    private final URL destroy;

    public Sound()
    {
        this.shoot = this.getClass().getClassLoader().getResource("game/obj/sound/shoot.wav");//link of sound
        this.hit = this.getClass().getClassLoader().getResource("game/obj/sound/hit.wav");//link of sound
        this.destroy = this.getClass().getClassLoader().getResource("game/obj/sound/destroy.wav");//link of sound
    }

//soundShoot function
    public void soundShoot()
    {
        play(shoot);
    }

//soundHit function
    public void soundHit()
    {
        play(hit);
    }

//soundDestroy function
    public void soundDestroy()
    {
        play(destroy);
    }
    //play function
    private void play(URL url)
    { //Using try catch function(exception handling).
        try
        { //Using actual functioning of audio system
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            audioIn.close();
            clip.start();


        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) //checked exception used
        {
            System.err.println(e);
        }
    }
}


