package stay.with.me.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stay.with.me.api.model.dto.FileDto;
import stay.with.me.api.model.dto.HouseFileDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.service.HouseService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private enum IMGEXT {
        jpg, jpeg, png, webp;
        public static boolean isValidExtension(String ext) {
            for(IMGEXT validExt : values()) {
                if(validExt.name().equalsIgnoreCase(ext)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final AmazonS3Client amazonS3Client;
    private final HouseService houseService;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadImage(@RequestParam("file") MultipartFile file, @ModelAttribute FileDto param) {
        try {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String imageBucket = System.getenv("S3_IMAGE_BUCKET");
            String fileBucket = System.getenv("S3_FILE_BUCKET");
            String bucket = "", width = "", height = "", saveFileName = "";
            String fileName = file.getOriginalFilename();
            String[] fileNames = fileName.split("\\.");
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            BufferedImage image = ImageIO.read(file.getInputStream());

            if(image == null) return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), "이미지가 존재하지 않습니다.", null, HttpStatus.INTERNAL_SERVER_ERROR);

            if("image".equals(param.getType())) {
                if(!IMGEXT.isValidExtension(fileNames[1])) {
                    return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(),"이미지 파일 확장자를 확인해 주세요.", null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                bucket = imageBucket;
            } else if("file".equals(param.getType())) {
                bucket = fileBucket;
            }

            if(image.getWidth() < 1024) width = String.valueOf(image.getWidth());
            else width = "1024";
            if(image.getHeight() < 1024) height = String.valueOf(image.getHeight());
            else height = "1024";

            saveFileName = fileNames[0] + "_" + width + "_" + height + "." + fileNames[1];
            String fileUrl = "https://" + bucket + ".s3."+region+".amazonaws.com/" + fileName;

            log.debug("image width: " + width + " height: " + height);
            log.debug("file name: " + fileName);

            if("house".equals(param.getMenu())) {
                HouseFileDto fileDto = new HouseFileDto();
                fileDto.setHouseFileName(fileNames[0]);
                fileDto.setHouseFileExtension(fileNames[1]);
                fileDto.setHouseFileWidth(Integer.parseInt(width));
                fileDto.setHouseFileHeight(Integer.parseInt(height));
                fileDto.setHouseDetailId(param.getId());
                fileDto.setHouseFilePath(fileUrl);
                fileDto.setCreatedBy(userId);
                houseService.createFile(fileDto);
            }
            amazonS3Client.putObject(bucket,saveFileName,file.getInputStream(),metadata);
            Map<String, Object> data = Map.of("result", fileUrl);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
