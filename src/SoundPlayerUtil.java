import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 声音播放类
 */
public class SoundPlayerUtil {
    public File file;
    public AudioInputStream stream;
    public AudioFormat format;
    DataLine.Info info;
    Clip clip;

    /**
     * 加载声音文件，支持wav、mp3等声音文件
     *
     * @param filePath 声音文件的路径
     */
    public void loadSound(String filePath) {
        file = new File(filePath);
        try {
            stream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        format = stream.getFormat();
    }

    /**
     * 播放音乐
     *
     * @param isLoop 表示是否循环播放音乐，如果传入的是true则表示循环播放
     */
    public void playSound(boolean isLoop) {
        info = new DataLine.Info(Clip.class, format);
        try {
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        if (isLoop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);// 添加该句代码可以循环播放
        }
        clip.start();
    }

}
