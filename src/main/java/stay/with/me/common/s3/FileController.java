package stay.with.me.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final AmazonS3Client amazonS3Client;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        try {
            String imageBucket = System.getenv("S3_IMAGE_BUCKET");
            String fileBucket = System.getenv("S3_FILE_BUCKET");
            String bucket = "", width = "", height = "", saveFileName = "";
            String[] fileName = file.getOriginalFilename().split(".");
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            if("image".equals(type)) bucket = imageBucket;
            else if("file".equals(type)) bucket = fileBucket;
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image != null) {
                if(image.getWidth() < 1024) width = String.valueOf(image.getWidth());
                else width = "1024";
                if(image.getHeight() < 1024) height = String.valueOf(image.getHeight());
                else height = "1024";
            }
            saveFileName = fileName[0] + "_" + width + "_" + height + "." + fileName[1];
            String fileUrl = "https://" + bucket + "/" + saveFileName;
            amazonS3Client.putObject(bucket,saveFileName,file.getInputStream(),metadata);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
