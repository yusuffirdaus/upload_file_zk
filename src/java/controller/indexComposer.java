package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

/**
 *
 * @author yusuf
 */
public class indexComposer extends GenericForwardComposer {

    private Image img;
    private Label msgLb;
    private static final int FILE_SIZE = 1000;// 100k
    private static final String xdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private static final String SAVE_PATH = "/home/yusuf/Documents/upload/" + xdate + "/";
//   private static final String SAVE_PATH = "c:\\myfile\\media\\" + xdate + "/"; //ganti jika windows

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$uploadBtn() {
        msgLb.setValue("");

        Media media = Fileupload.get();
        if (media == null) {
            msgLb.setValue("please select a file");
            return;
        }
        String type = media.getContentType().split("/")[0];
        if (type.equals("image")) {
            if (media.getByteData().length > FILE_SIZE * 1024) {
                msgLb.setValue("File size limit " + FILE_SIZE + "k");
                return;
            }
            org.zkoss.image.Image picture = (org.zkoss.image.Image) media;
            img.setContent(picture);
        }
        saveFile(media);
    }

    private void saveFile(Media media) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            InputStream fin = media.getStreamData();
            in = new BufferedInputStream(fin);

            File baseDir = new File(SAVE_PATH);

            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }

            File file = new File(SAVE_PATH + media.getName());

            OutputStream fout = new FileOutputStream(file);
            out = new BufferedOutputStream(fout);
            byte buffer[] = new byte[1024];
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }
            msgLb.setValue("sucessed upload :" + media.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
