package FinalProject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
Desc:  Class that holds data for a single image for transfer through network
Author: John Wang
Date: 3/26/2020
*/

public class ImageChatItem extends ChatItem{
    //image data
    transient BufferedImage image;
    //image name
    private String imageName;

    //constructor
    ImageChatItem(BufferedImage image, String imageName, UserItem userParent) {
        super( userParent);
        this.type = ItemType.CHATIMAGE;
        this.image = image;
        this.imageName = imageName;
    }


    //overrides for image serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }

    //accessors
    public BufferedImage getImage(){
        return image;
    }
    public String getImageName(){
        return imageName;
    }


}
