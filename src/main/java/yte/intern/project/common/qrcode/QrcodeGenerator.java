package yte.intern.project.common.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class QrcodeGenerator {

    private final String eventName;
    private final String firstName;
    private final String lastName;

    public void generateQrcode() throws WriterException, IOException {
        String data = "Dear " + this.firstName + " " + this.lastName + ". You have registered to " + this.eventName + ".";
        String imageName = this.firstName + this.lastName + this.eventName;
        String path = "C:\\Users\\yavuz\\Desktop\\TUBITAK\\Project\\project\\front-end\\public\\%s.jpg".formatted(imageName);

        BitMatrix matrix = new MultiFormatWriter()
                .encode(data, BarcodeFormat.QR_CODE, 350, 350);

        MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));
    }

}
