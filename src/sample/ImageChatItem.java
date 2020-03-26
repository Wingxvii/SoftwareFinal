package sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageChatItem extends ChatItem{
    transient BufferedImage image;

    ImageChatItem(BufferedImage image, UserItem userParent) {
        super( userParent);
        this.type = ItemType.CHATIMAGE;
        this.image = image;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }

    public BufferedImage getImage(){
        return image;

    }


}